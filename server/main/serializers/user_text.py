from rest_framework import serializers

from main.models import UserText, Text


class UserTextSerializer(serializers.ModelSerializer):
    content = serializers.CharField(write_only=True)

    class Meta:
        model = UserText
        fields = ('id', 'text', 'content', )

    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.fields['id'].read_only = True
        self.fields['text'].read_only = True

    def create(self, validated_data):
        content = validated_data.pop("content")
        text, _ = Text.objects.get_or_create(content=content)
        validated_data["text"] = text
        return super().create(validated_data)

    def update(self, instance, validated_data):
        return super().update(instance, validated_data)
