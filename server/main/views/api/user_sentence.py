from django.db.models import Count

from rest_framework.mixins import (RetrieveModelMixin, ListModelMixin,
                                   DestroyModelMixin)
from rest_framework.viewsets import GenericViewSet

from main.models import Sentence
from main.serializers import SentenceSerializer


class UserSentenceViewSet(ListModelMixin, RetrieveModelMixin,
                          DestroyModelMixin, GenericViewSet):
    serializer_class = SentenceSerializer

    def perform_destroy(self, instance):
        instance.usersentence_set.filter(text__user=self.request.user).delete()

    def get_queryset(self):
        res = Sentence.objects.filter(
            usersentence__text__user=self.request.user).distinct().annotate(
                Count('usersentence')).order_by("-usersentence__count")
        return res
