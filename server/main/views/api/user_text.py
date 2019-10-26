from celery.result import AsyncResult

from rest_framework.mixins import (RetrieveModelMixin, CreateModelMixin,
                                   ListModelMixin)
from rest_framework.viewsets import GenericViewSet

from main.models import UserText
from main.serializers import UserTextSerializer
from main.tasks import split_and_register_sentences


class UserTextViewSet(ListModelMixin, RetrieveModelMixin, CreateModelMixin,
                      GenericViewSet):
    serializer_class = UserTextSerializer
    queryset = UserText.objects.all()

    def get_queryset(self):
        return UserText.objects.filter(user=self.request.user)

    def perform_create(self, serializer):
        serializer.save(user=self.request.user)

    def create(self, request, *args, **kwargs):
        res = super().create(request, *args, **kwargs)

        task_id = split_and_register_sentences.delay(res.data["id"])
        result = AsyncResult(task_id)
        print('result:', result, ' : ', result.state, ' : ', result.ready())

        return res
