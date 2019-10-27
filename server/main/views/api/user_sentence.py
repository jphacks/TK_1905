from django.db.models import Count

from rest_framework.mixins import (RetrieveModelMixin, ListModelMixin,
                                   DestroyModelMixin)
from rest_framework.viewsets import GenericViewSet
from django_filters import rest_framework as filters

from main.models import Sentence
from main.serializers import SentenceSerializer


class UserSentenceFilter(filters.FilterSet):
    score__gt = filters.CharFilter(field_name='score', lookup_expr='gt')
    score__lt = filters.CharFilter(field_name='score', lookup_expr='lt')


class UserSentenceViewSet(ListModelMixin, RetrieveModelMixin,
                          DestroyModelMixin, GenericViewSet):
    serializer_class = SentenceSerializer
    filter_class = UserSentenceFilter

    def perform_destroy(self, instance):
        instance.usersentence_set.filter(text__user=self.request.user).delete()

    def get_queryset(self):
        res = Sentence.objects.filter(
            usersentence__text__user=self.request.user).distinct().annotate(
                Count('usersentence')).order_by("-usersentence__count",
                                                "-score")
        return res
