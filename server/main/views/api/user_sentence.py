from django.db.models import Count, Q

from rest_framework.mixins import (RetrieveModelMixin, ListModelMixin,
                                   DestroyModelMixin)
from rest_framework.viewsets import GenericViewSet
from django_filters import rest_framework as filters

from main.models import Sentence
from main.serializers import SentenceSerializer


class UserSentenceFilter(filters.FilterSet):
    def get_by_score__gt(self, queryset, name, value):
        return queryset.filter(Q(score__gt=value) | Q(score=.0)).distinct()

    score__gt = filters.NumberFilter(method='get_by_score__gt')
    score__lt = filters.NumberFilter(field_name='score', lookup_expr='lt')


class UserSentenceViewSet(ListModelMixin, RetrieveModelMixin,
                          DestroyModelMixin, GenericViewSet):
    serializer_class = SentenceSerializer
    filter_class = UserSentenceFilter

    # def get_serializer(self, *args, **kwargs):
        # serializer = super().get_serializer(self, *args, **kwargs)
        # serializer.user = self.request.user
        # return serializer

    def perform_destroy(self, instance):
        instance.usersentence_set.filter(text__user=self.request.user).delete()

    def get_queryset(self):
        res = Sentence.objects.filter(
            usersentence__text__user=self.request.user).distinct().annotate(
                Count('usersentence')).order_by("-usersentence__count",
                                                "-score")
        return res
