from django.urls import path

from channels.routing import ProtocolTypeRouter, URLRouter
from channels.auth import AuthMiddlewareStack

from config.middleware import TokenAuthMiddleware
from main.consumers import RoomConsumer


TokenAuthMiddlewareStack = lambda inner: TokenAuthMiddleware(AuthMiddlewareStack(inner))


application = ProtocolTypeRouter({
    "websocket": TokenAuthMiddlewareStack(
        URLRouter([
            path('ws/room/<str:pk>/', RoomConsumer),
        ])
    )
})
