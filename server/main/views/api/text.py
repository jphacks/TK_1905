from rest_framework.mixins import (RetrieveModelMixin, CreateModelMixin,
                                   ListModelMixin)
from rest_framework.viewsets import GenericViewSet

from main.models import Text
from main.serializers import TextSerializer


class TextViewSet(ListModelMixin, RetrieveModelMixin, CreateModelMixin,
                  GenericViewSet):
    serializer_class = TextSerializer
    queryset = Text.objects.all()

    def get_queryset(self):
        return Text.objects.filter(user=self.request.user)

    def perform_create(self, serializer):
        serializer.save(user=self.request.user)
