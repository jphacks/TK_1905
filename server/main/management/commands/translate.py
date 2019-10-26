from django.core.management.base import BaseCommand

from main.utils import translate


class Command(BaseCommand):
    help = 'translate'

    def add_arguments(self, parser):
        parser.add_argument(dest='text', help='text')

    def handle(self, *args, **options):
        print('translate')
        text = options['text']

        print(translate(text))
