from django.core.management.base import BaseCommand


class Command(BaseCommand):
    help = 'seed'

    def handle(self, *args, **options):
        print('seed')
