from django.core.management.base import BaseCommand

from main.utils import translate, doc2vec


class Command(BaseCommand):
    help = 'translate'

    def add_arguments(self, parser):
        parser.add_argument(dest='text', help='text')

    def handle(self, *args, **options):
        print('translate')
        text = options['text']

        text_en = translate(text)
        text_ja = translate(text_en, target="ja")
        print(text)
        print(text_en)
        print(text_ja)

        print(doc2vec(text, text_ja))
