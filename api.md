# Auth1 HTTP API Documentation

## Endpoints

### Register

Method: `POST`

Resource: `/register`

#### Parameters

Name     | Value 
-------: | ---
username | the username of the new user to be created
email    | the email of the new user to be created
password | the raw (unhashed) password of the new user

#### Response

##### Response JSON

Key    | Value 
-----: | ---
result | RegistrationResult corresponding to outcome.

##### RegistrationResult

Value                 | Meaning 
--------------------: | ---
SUCCESS               | The account was successfully created and is automatically verified.
SUCCESS_CONFIRM_EMAIL | The account was successfully created, but is not verified.
USERNAME_DUPLICATE    | The account was not created, because an account with this username already exists.
EMAIL_DUPLICATE       | The account was not created, because an account with this email already exists.
EMAIL_REQUIRED        | The account was not created, because an email is required for registration and one was not provided.
USERNAME_REQUIRED     | The account was not created, because a username is required for registration and one was not provided.
PASSWORD_WEAK         | The account was not created, because the password was deemed too weak for account creation.

##### Example Response

```
{'result': 'SUCCESS'}
```

### Check Authentication Token

Method: `POST`

Resource: `/checkAuthToken`

#### Parameters

Name  | Value 
----: | ---
token | the string of the user authentication token to check

##### Response JSON

Key    | Value 
-----: | ---
valid  | 'true' if the token was valid, 'false' otherwise
userId | if the token is valid, the id of the user associated with the token

##### Example Responses

```
{'valid': 'false'}
```

```
{'valid': 'true', 'userId': 1}
```

### Request TOTP Secret

Method: `POST`

Resource: `/requestTotpSecret`