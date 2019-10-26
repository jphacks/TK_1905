from django.contrib import admin
from django.db.models import Model
from django.contrib.admin.sites import AlreadyRegistered
from django.utils.safestring import mark_safe

from main import models
from main.models import User, Tweet
from main.utils import register_admin, get_field_names


class TweetInline(admin.StackedInline):
    model = Tweet
    extra = 1
    filter_horizontal = ('tags', )


class UserAdmin(admin.ModelAdmin):
    inlines = [TweetInline]
    search_fields = ['id', 'name', 'email']

    def get_list_display(self, request):
        list_display = get_field_names(User)
        list_display.append("image_show")
        return list_display

    @mark_safe
    def image_show(self, row):
        if row.icon:
            return f'<img src="{row.icon.url}" style="width:100px;height:auto;">'
        return ""


class TweetAdmin(admin.ModelAdmin):
    list_display = get_field_names(Tweet)
    search_fields = ['name']
    filter_horizontal = ('tags', )


admin.site.register(User, UserAdmin)
admin.site.register(Tweet, TweetAdmin)

# 残りのモデルを全て登録
for name in dir(models):
    obj = getattr(models, name)
    if not isinstance(obj, type):
        continue

    if issubclass(getattr(models, name), Model):
        try:
            register_admin(obj)
        except AlreadyRegistered:
            pass
