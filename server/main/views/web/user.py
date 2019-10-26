from django.urls import reverse_lazy
from django.views.generic import TemplateView, FormView

from main.forms import UserForm

from .base import OnlyLoginUserMixin


class UserDetailView(OnlyLoginUserMixin, TemplateView):
    template_name = "main/user_detail.html"


class UserFormView(OnlyLoginUserMixin, FormView):
    form_class = UserForm
    template_name = 'main/user_form.html'
    success_url = reverse_lazy('main:profile_detail')

    def get_form_kwargs(self):
        kwargs = super().get_form_kwargs()
        kwargs['instance'] = self.request.user
        return kwargs

    def form_valid(self, form):
        form.save()
        return super().form_valid(form)
