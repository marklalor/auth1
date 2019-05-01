from io import BytesIO

from flask import Flask, url_for, redirect, send_file
from auth1api import Auth1Client

import functools

from flask import (g, request, make_response, render_template, flash)

auth1 = Auth1Client('http://localhost:50505')

app = Flask(__name__)
app.secret_key = b'_5#y2L"F4Q8z\n\xec]/'

def login_required(view):
    @functools.wraps(view)
    def wrapped_view(**kwargs):
        error_response = redirect(url_for('login_page'))
        if 'token' not in request.cookies:
            return error_response
        else:
            token = request.cookies['token']
            validation_response = auth1.check_auth_token(token)
            if validation_response['valid']:
                g.token = request.cookies['token']
                g.user_id = validation_response['username']
            else:
                return error_response
        return view(**kwargs)
    return wrapped_view


@app.route('/')
@login_required
def main():
    return make_response(render_template('index.html', user_id=g.user_id))

@app.route('/register', methods=['GET'])
def register_page():
    return make_response(render_template('register.html'))


@app.route('/register', methods=['POST'])
def register():
    email = request.form['email'] if 'email' in request.form else ''
    username = request.form['username'] if 'username' in request.form else ''
    password = request.form['password'] if 'password' in request.form else ''

    register_response = auth1.register(username, email, password)

    if register_response['result'] == 'SUCCESS':
        flash('You successfully created an account')
        return redirect(url_for('login'))

    if register_response['result'] == 'USERNAME_DUPLICATE':
        flash("Username '%s' already exists" % username)
        return redirect(url_for('register'))

    if register_response['result'] == 'EMAIL_DUPLICATE':
        flash("Email '%s' already exists" % email)
        return redirect(url_for('register'))

    flash('Unable to create account. Please check that all fields are filled out and try again later')
    return redirect(url_for('register'))


@app.route('/login', methods=['GET'])
def login_page():
    return make_response(render_template('login.html'))


@app.route('/logout', methods=['GET'])
def logout():
    flash('Logged out.')
    resp = redirect(url_for('login'))
    resp.set_cookie('token', '', expires=0)
    return resp


@app.route('/login', methods=['POST'])
def login():
    username = request.form['username'] if 'username' in request.form else ''
    password = request.form['password'] if 'password' in request.form else ''
    totp_code = request.form['totp'] if 'totp' in request.form else ''

    login_response = auth1.login(None, username, None, password, totp_code)

    if login_response['resultType'] == 'SUCCESS':
        token = login_response['token']['tokenValue']
        resp = redirect(url_for('main'))
        resp.set_cookie('token', token)
        return resp
    else:
        responses = {
            'USER_DOES_NOT_EXIST': 'Bad username or password, please try again.',
            'TOO_MANY_REQUESTS': 'Too many requests made, please try again later.',
            'BAD_PASSWORD': 'Bad username or password, please try again.',
            'TOTP_REQUIRED': 'Two-factor authentication required, please provide TOTP code.',
            'BAD_TOTP': 'TOTP code was incorrect, please try again.',
            'NOT_VERIFIED': 'The account you\'re trying to log in to is unverified',
            'ACCOUNT_LOCKED': 'Account locked, please contact an server administrator.'
        }
        flash(responses[login_response['resultType']])
        return redirect(url_for('login'))


@app.route('/createTOTP', methods=['GET'])
@login_required
def create_totp():
    response = auth1.requestTotpSecret(g.token)
    if response['resultType'] == 'SUCCESS':
        g.secret = response['tentativeTotpSecret']
        return make_response(render_template('setUpTOTP.html', secret=g.secret))


@app.route('/confirmTOTP', methods=['POST'])
@login_required
def confirm_totp():
    response = auth1.confirmTotpSecret(g.token, request.form['code'])
    if response['resultType'] == 'SUCCESS':
        return make_response(render_template('message.html', title='TOTP Confirmed',
                                             message=f'TOTP is now set up and ready to use with your account.',
                                             link=url_for('main'), link_message='Back to main page.'))
    elif response['resultType'] == 'INVALID_CODE':
        flash("Invalid TOTP code")
        return make_response(render_template('setUpTOTP.html', secret=g.secret))


@app.route('/totp_qr', methods=['GET'])
def totp_qr():
    import qrcode
    qr = qrcode.QRCode(
        version=1,
        error_correction=qrcode.constants.ERROR_CORRECT_L,
        box_size=10,
        border=4,
    )
    secret = request.args.get('secret')
    qr.add_data(f'otpauth://totp/Auth1 Example:email@google.com?secret={secret}&issuer=Auth1 Example')
    qr.make(fit=True)

    img_io = BytesIO()
    img = qr.make_image(fill_color="black", back_color="transparent")
    img.save(img_io, 'PNG')
    img_io.seek(0)
    return send_file(img_io, mimetype='image/png')


if __name__ == '__main__':
    app.config['TEMPLATES_AUTO_RELOAD'] = True
    app.run()
