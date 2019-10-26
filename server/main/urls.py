from django.urls import path, include
from django.conf import settings
from django.contrib.auth.views import logout_then_login

from rest_framework.routers import DefaultRouter, APIRootView
from rest_framework_jwt.views import obtain_jwt_token, refresh_jwt_token, verify_jwt_token
from rest_framework_swagger.views import get_swagger_view

from . import views

schema_view = get_swagger_view()

router = DefaultRouter()
router.APIRootView = APIRootView
router.register('users', views.UserViewSet, base_name='user')
router.register('user/texts', views.TextViewSet, base_name='user_text')

app_name = 'main'
urlpatterns = [
    path('', schema_view),
    path('api/', include(router.urls)),
    path('api/register/uuid/', views.RegisterUUIDView.as_view()),
    path('api/auth/refresh/', refresh_jwt_token),
    path('api/auth/verify/', verify_jwt_token),
    path('api/auth/uuid/', views.AuthUUIDView.as_view()),
    path('api/user/', views.UserView.as_view()),
]

if settings.DEBUG:

    urlpatterns.extend([
        path('api/register/dummy/', views.RegisterDummyUserView.as_view()),
        path('api/auth/user/', obtain_jwt_token),
    ])
