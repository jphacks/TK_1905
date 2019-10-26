from rest_framework import serializers

from main.models import Sentence


class SentenceSerializer(serializers.ModelSerializer):
    class Meta:
        model = Sentence
        fields = ('content_jp', 'content_en', )

    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
