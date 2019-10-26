from django.forms import Form
from django.forms.fields import EmailField, CharField
from django.forms.widgets import TextInput, PasswordInput

from main.models import User

from .base import BootstrapFormMixin


class SignupForm(BootstrapFormMixin, Form):
    name = CharField(
        max_length=256,
        required=True,
        widget=TextInput(attrs={'placeholder': 'ユーザーネーム'}))
    email = EmailField(
        max_length=256,
        required=True,
        widget=TextInput(attrs={'placeholder': 'メールアドレス'}))
    password = CharField(
        min_length=8,
        max_length=256,
        required=True,
        widget=PasswordInput(attrs={'placeholder': 'パスワード(8文字以上)'}))

    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)

    def create_user(self):
        if 'name' in self.cleaned_data:
            name = self.cleaned_data['name']
        else:
            name = None
        email = self.cleaned_data['email']
        password = self.cleaned_data['password']
        user = None
        try:
            if name:
                user = User.objects.create_user(email, password, name=name)
            else:
                user = User.objects.create_user(email, password)
        except:
            self.add_error('password', 'ユーザーの作成に失敗しました.')
        return user

    def clean_email(self):
        email = self.cleaned_data['email']
        if User.objects.filter(email=email).count() > 0:
            self.add_error('email', 'そのメールアドレスは既に使われています.')
        return email
