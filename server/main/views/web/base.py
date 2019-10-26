from django.contrib.auth.mixins import UserPassesTestMixin


class OnlyLoginUserMixin(UserPassesTestMixin):
    def test_func(self):
        return self.request.user.is_authenticated

    def get(self, request, *args, **kwargs):
        return super().get(request, *args, **kwargs)
