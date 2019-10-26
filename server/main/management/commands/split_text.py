from django.core.management.base import BaseCommand

from main.utils import split_text


class Command(BaseCommand):
    help = 'split_text'

    def add_arguments(self, parser):
        parser.add_argument(dest='text', help='text')

    def handle(self, *args, **options):
        print('split_text')
        text = options['text']

        print(split_text(text))
