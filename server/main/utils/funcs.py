import uuid
from datetime import date, datetime, timedelta

import numpy as np

from django.utils import timezone


def json_serial(obj):
    if isinstance(obj, (datetime, date)):
        return obj.isoformat()
    elif isinstance(obj, (uuid.UUID)):
        return str(obj)
    elif isinstance(obj, np.ndarray):
        return obj.tolist()
    raise TypeError("Type %s not serializable" % type(obj))


def get_next_minute_datetime(d):
    return timezone.localtime(d + timedelta(minutes=1) - timedelta(
        seconds=d.second) - timedelta(microseconds=d.microsecond))


def get_prev_minute_datetime(d):
    return timezone.localtime(d - timedelta(seconds=d.second) -
                              timedelta(microseconds=d.microsecond))
