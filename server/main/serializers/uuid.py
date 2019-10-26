from rest_framework import serializers


class UUIDSerializer(serializers.Serializer):
    uuid = serializers.UUIDField()
