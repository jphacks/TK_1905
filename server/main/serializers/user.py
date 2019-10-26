from rest_framework import serializers

from main.models import User


class UserSerializer(serializers.ModelSerializer):
    icon_base64 = serializers.CharField(write_only=True, required=False)
    icon_url = serializers.SerializerMethodField(read_only=True)

    class Meta:
        model = User
        fields = ('id', 'name', 'icon_url', 'icon_base64', )

    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.fields['id'].read_only = True
        self.fields['id'].read_only = True

    def update(self, instance, validated_data):
        if 'icon_base64' in validated_data:
            icon_base64 = validated_data.pop('icon_base64')
            instance.save_icon_with_base64(icon_base64)
        return super().update(instance, validated_data)

    def get_icon_url(self, obj):
        if obj.icon is None:
            return None
        try:
            return obj.icon.url
        except ValueError:
            return None
