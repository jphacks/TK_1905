from django.forms import ModelForm

from main.models import User

from .base import BootstrapFormMixin


class UserForm(BootstrapFormMixin, ModelForm):
    class Meta:
        model = User
        fields = ['name', 'email']

    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.fields['name'].required = False
        self.fields['email'].required = False
