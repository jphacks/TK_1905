from rest_framework.mixins import (RetrieveModelMixin, ListModelMixin)
from rest_framework.viewsets import GenericViewSet

from main.models import Sentence
from main.serializers import SentenceSerializer


class UserSentenceViewSet(ListModelMixin, RetrieveModelMixin, GenericViewSet):
    serializer_class = SentenceSerializer

    def get_queryset(self):
        res = Sentence.objects.filter(
            usersentence__text__user=self.request.user).distinct()
        return res
