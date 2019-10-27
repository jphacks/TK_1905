from rest_framework import serializers

from main.models import Sentence


class SentenceSerializer(serializers.ModelSerializer):
    spoken_count = serializers.SerializerMethodField(read_only=True)

    class Meta:
        model = Sentence
        fields = ('content_jp', 'content_en', 'score', 'spoken_count', )

    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.fields['score'].read_only = True

    def get_spoken_count(self, obj):
        return obj.usersentence_set.all().count()
        # return obj.usersentence_set.filter(text__user=self.user).count()
