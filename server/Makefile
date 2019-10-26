include .env
IMAGE:=django-project-default

build:
	docker build ./ -t $(IMAGE):latest

run:
	docker run --name $(IMAGE) --rm -v $PWD:/var/www/html/ -p 8000:8000 $(IMAGE) python -u manage.py runserver 0.0.0.0:8000

run-d:
	docker run --name $(IMAGE) --rm -d -v $PWD:/var/www/html/ -p 8000:8000 $(IMAGE) python -u manage.py runserver 0.0.0.0:8000

logs:
	docker logs -ft $(IMAGE)

kill:
	docker kill $(IMAGE)
