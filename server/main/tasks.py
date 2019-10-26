from celery import shared_task
import time

from main.models import Text, Sentence
from main.utils import split_text, translate


@shared_task
def add(x1, x2):
    time.sleep(10)
    y = x1 + x2
    print('処理完了')
    return y


@shared_task
def split_and_register_sentences(text_id):
    text = Text.objects.get(id=text_id)
    sentences = split_text(text.text)
    for sentence in sentences:
        sentence_en = translate(sentence)
        Sentence.objects.get_or_create(text=text,
                                       text_jp=sentence,
                                       text_en=sentence_en)
    return True
