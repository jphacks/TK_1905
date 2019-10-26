from django.forms import Form
from django.forms.fields import EmailField, CharField
from django.forms.widgets import TextInput, PasswordInput
from django.contrib.auth import authenticate

from main.models import User

from .base import BootstrapFormMixin


class SigninForm(BootstrapFormMixin, Form):
    email = EmailField(
        max_length=256,
        required=True,
        widget=TextInput(attrs={'placeholder': 'メールアドレス'}))
    password = CharField(
        max_length=256,
        required=True,
        widget=PasswordInput(attrs={'placeholder': 'パスワード'}))

    def clean_email(self):
        email = self.cleaned_data['email']
        if User.objects.filter(email=email).count() == 0:
            self.add_error('password', 'メールアドレスかパスワードが正しくありません。')
        return email

    def get_authenticated_user(self):
        user = authenticate(
            username=self.cleaned_data['email'],
            password=self.cleaned_data['password'])
        if user is None:
            self.add_error('password', 'メールアドレスかパスワードが正しくありません。')
        return user
