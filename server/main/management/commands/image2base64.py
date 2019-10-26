import base64

from django.core.management.base import BaseCommand


class Command(BaseCommand):
    help = 'image2base64'

    def add_arguments(self, parser):
        parser.add_argument(
            dest='img_path',
            help='img_path'
        )

    def handle(self, *args, **options):
        print('image2base64')
        img_path = options['img_path']
        print(img_path)

        with open(img_path, 'rb') as f:
            img_b64 = base64.encodestring(f.read())

        img_b64_str = img_b64.decode('utf8')
        print(repr(img_b64_str))

        base64.b64decode(img_b64_str.encode())
