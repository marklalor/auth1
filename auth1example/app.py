from flask import Flask, url_for, redirect
from auth1api import Auth1Client

import functools

from flask import (g, request, make_response, render_template)


client = Auth1Client('http://localhost:3000')

app = Flask(__name__)


def login_required(view):
    @functools.wraps(view)
    def wrapped_view(**kwargs):
        error_response = make_response()
        error_response.status_code = 401
        if 'token' not in request.cookies:
            return error_response
        else:
            token = request.cookies['token']
            validation_response = client.check_auth_token(token)
            if validation_response['result'] != 'SUCCESS':
                g.user_id = validation_response['user_id']
            else:
                return error_response
        return view(**kwargs)
    return wrapped_view


@app.route('/')
@login_required
def main():
    return make_response(render_template('index.html', user_id=g.user_id))


@app.route('/login', methods=['GET'])
def login_page():
    return make_response(render_template('login.html'))


@app.route('/logout', methods=['GET'])
def logout():
    return make_response(render_template('login.html', message='Logged out.'))


@app.route('/login', methods=['POST'])
def login():
    username = request.form['username'] if 'username' in request.form else ''
    password = request.form['password'] if 'password' in request.form else ''

    login_response = client.login(None, username, None, password)

    if login_response['result'] == 'SUCCESS':
        token = login_response['loginToken']['value']
        resp = redirect(url_for('main'))
        resp.set_cookie('token', token)
        return resp
    else:
        return make_response(render_template('login.html', message='Bad username or password, please try again.'))


if __name__ == '__main__':
    app.config['TEMPLATES_AUTO_RELOAD'] = True
    app.run()
