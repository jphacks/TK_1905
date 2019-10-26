import functools
import time
import os

from django.db.models.fields.files import ImageField, FileField


def timeit(f):
    @functools.wraps(f)
    def wrapper(*args, **kwargs):
        started_at = time.time()
        res = f(*args, **kwargs)
        elapsed_time = time.time() - started_at
        print('[timeit: {proc_name}]: {elapsed_time}s'.format(
            proc_name=f.__name__, elapsed_time=elapsed_time))
        return res
    return wrapper


def delete_previous_file(f):
    @functools.wraps(f)
    def get_file_paths(obj):
        file_field_names = []
        for f in obj._meta.fields:
            if f.__class__ in [ImageField, FileField]:
                file_field_names.append(f.name)
        previous_file_paths = []
        for name in file_field_names:
            f = getattr(obj, name)
            if f:
                previous_file_paths.append(f.path)
        return previous_file_paths

    def wrapper(*args, **kwargs):
        self = args[0]

        try:
            obj = self.__class__.objects.get(pk=self.pk)
        except self.__class__.DoesNotExist:
            return f(*args, **kwargs)

        previous_file_paths = get_file_paths(obj)
        res = f(*args, **kwargs)

        try:
            now_obj = self.__class__.objects.get(pk=self.pk)
            now_file_paths = get_file_paths(now_obj)
        except self.__class__.DoesNotExist:
            now_file_paths = []

        for previous_file_path in previous_file_paths:
            if previous_file_path not in now_file_paths:
                try:
                    os.remove(previous_file_path)
                except FileNotFoundError:
                    pass
        return res
    return wrapper
