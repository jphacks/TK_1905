from django.urls import path, include
from django.conf import settings
from django.contrib.auth.views import logout_then_login

from rest_framework.routers import DefaultRouter, APIRootView
from rest_framework_jwt.views import obtain_jwt_token, refresh_jwt_token, verify_jwt_token

from . import views

router = DefaultRouter()
router.APIRootView = APIRootView
router.register('users', views.UserViewSet, base_name='user')

app_name = 'main'
urlpatterns = [
    path('', views.IndexView.as_view(), name='index'),
    path('api/', include(router.urls)),
    path('api/register/uuid/', views.RegisterUUIDView.as_view()),
    path('api/auth/refresh/', refresh_jwt_token),
    path('api/auth/verify/', verify_jwt_token),
    path('api/auth/uuid/', views.AuthUUIDView.as_view()),
    path('api/user/', views.UserView.as_view()),
    path('signup/', views.SignupView.as_view(), name='signup'),
    path('signin/', views.SigninView.as_view(), name='signin'),
    path('signout/', logout_then_login, name='signout'),
    path('profile/', views.UserDetailView.as_view(), name='profile_detail'),
    path('profile/edit/', views.UserFormView.as_view(), name='profile_edit'),
]

if settings.DEBUG:
    from rest_framework_swagger.views import get_swagger_view

    schema_view = get_swagger_view()

    urlpatterns.extend([
        path('api/register/dummy/', views.RegisterDummyUserView.as_view()),
        path('api/auth/user/', obtain_jwt_token),
        path('schema/', schema_view),
    ])
