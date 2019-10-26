from django.contrib import admin

from .funcs import get_field_names


def create_admin(cls):
    return type("%sAdmin" % cls.__name__, (admin.ModelAdmin, ),
                {"list_display": get_field_names(cls)})


def register_admin(cls):
    admin.site.register(cls, create_admin(cls))
