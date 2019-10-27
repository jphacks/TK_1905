from django.core.management.base import BaseCommand

from main.utils import doc2vec


class Command(BaseCommand):
    help = 'calc_vec_diff'

    def add_arguments(self, parser):
        parser.add_argument(dest='text1', help='text')
        parser.add_argument(dest='text2', help='text')

    def handle(self, *args, **options):
        print('calc_vec_diff')
        text1 = options['text1']
        text2 = options['text2']

        print(doc2vec(text1, text2))
