# README

## env

* python 3.6.x
  * pipenv
* redis

## setup

```bash
pipenv run pipenv install
pipenv run python manage.py migrate
```

## run

```bash
pipenv run python manage.py runserver
```

## create user

```bash
pipenv run python manage.py createsuperuser
```

## check api schema

```bash
# after run server
open localhost:8000/schema/ # need session login
```

## httpie

```bash
http post http://localhost:8000/api/register/dummy/
http post http://localhost:8000/api/auth/user/ email=test@test.com password=testuser

http post http://localhost:8000/api/auth/refresh/ token={token}
http post http://localhost:8000/api/auth/verify/ token={token}

http http://localhost:8000/api/user/ Authorization:"JWT {token}"
```

## wscat

### install

```bash
npm install -g wscat
```

### run

```bash
wscat -c localhost:8000/ws/room/test/ -H "Authorization:JWT {token}"
```
