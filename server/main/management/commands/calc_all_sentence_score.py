from django.core.management.base import BaseCommand

from main.utils import translate, doc2vec
from main.models import Sentence


class Command(BaseCommand):
    help = 'calc_all_sentence_score'

    def handle(self, *args, **options):
        print('calc_all_sentence_score')
        for sentence in Sentence.objects.filter(score=.0):
            sentence_jp = translate(sentence.content_en, target='ja')
            score = doc2vec(sentence.content_jp, sentence_jp)

            sentence.score = score
            sentence.memo = sentence_jp
            sentence.save()

            print(sentence.content_jp)
            print(sentence.content_en)
            print(sentence.memo)
            print(sentence.score)
            print("-" * 24)
