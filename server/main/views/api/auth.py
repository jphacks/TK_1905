from rest_framework import status
from rest_framework.response import Response
from rest_framework.generics import GenericAPIView
from rest_framework_jwt.settings import api_settings

from main.models import User
from main.serializers import TokenSerializer, UUIDSerializer

jwt_payload_handler = api_settings.JWT_PAYLOAD_HANDLER
jwt_encode_handler = api_settings.JWT_ENCODE_HANDLER


class AuthUUIDView(GenericAPIView):
    serializer_class = UUIDSerializer
    permission_classes = ()

    def post(self, request):
        serializer = self.get_serializer(data=request.data)
        serializer.is_valid(raise_exception=True)

        user = User.objects.filter(device_uuid=serializer.data['uuid']).first()

        if user is None:
            message = {
                'detail': 'そのユーザーは存在しません'
            }
            return Response(message, status=status.HTTP_400_BAD_REQUEST)

        payload = jwt_payload_handler(user)
        token = jwt_encode_handler(payload)
        serializer = TokenSerializer(data={'token': token})
        serializer.is_valid(raise_exception=True)
        return Response(serializer.data)
