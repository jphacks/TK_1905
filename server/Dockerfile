FROM python:3.6.6

RUN apt update -y
RUN apt upgrade -y

WORKDIR /var/www/html

RUN apt install -y locales locales-all

ENV LC_ALL=ja_JP.UTF-8
ENV LANG=ja_JP.UTF-8
ENV LANGUAGE=ja_JP.UTF-8

RUN pip install -U pip
RUN pip install pipenv

ADD Pipfile /var/www/html/
ADD Pipfile.lock /var/www/html/
RUN pipenv lock -r > requirements.txt
RUN pip install -r requirements.txt
RUN rm requirements.txt

ADD . /var/www/html/
