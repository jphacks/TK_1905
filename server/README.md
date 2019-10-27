# README

## env

* python 3.6.x
  * pipenv
* redis

## setup

### Mac

```bash
brew install mecab-ipadic
```

### ubuntu

```bash
sudo apt install mecab libmecab-dev mecab-ipadic-utf8
```

### all

```bash
pipenv run pipenv install
pipenv run python manage.py migrate
# need to set local/wikipedia/{jawiki.doc2vec.dbow300d.model|*.npy}
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
open localhost:8000 # need session login
```

## httpie

```bash
http post http://localhost:8000/api/register/dummy/
http post http://localhost:8000/api/auth/user/ email=test@test.com password=testuser

http post http://localhost:8000/api/auth/refresh/ token={token}
http post http://localhost:8000/api/auth/verify/ token={token}

http http://localhost:8000/api/user/ Authorization:"JWT {token}"
```
