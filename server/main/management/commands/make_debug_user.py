from django.core.management.base import BaseCommand

from main.models import User


class Command(BaseCommand):
    help = 'make_debug_user'

    def handle(self, *args, **options):
        print('make_debug_user')
        User.objects.create_superuser(
            email="test@test.com", password="testuser", name="test")
