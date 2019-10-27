from celery import shared_task

from main.models import UserText, UserSentence, Sentence
from main.utils import split_text, translate, doc2vec


@shared_task
def split_and_register_sentences(text_id):
    user_text = UserText.objects.get(id=text_id)
    sentences = split_text(user_text.text.content)
    for sentence_str in sentences:
        sentence = Sentence.objects.filter(content_jp=sentence_str).first()
        if sentence is None:
            sentence_en = translate(sentence_str)
            sentence_jp = translate(sentence_en, target='ja')
            score = doc2vec(sentence_str, sentence_jp)
            sentence = Sentence.objects.create(content_jp=sentence_str,
                                               content_en=sentence_en,
                                               score=score,
                                               memo=sentence_jp)
        UserSentence.objects.create(text=user_text, sentence=sentence)
    return True
