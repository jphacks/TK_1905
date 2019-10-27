from django.db import models


class Sentence(models.Model):
    content_jp = models.TextField(primary_key=True)
    content_en = models.TextField(null=True)
    score = models.FloatField(default=0)

    memo = models.TextField(null=True)
    created_at = models.DateTimeField(auto_now_add=True)
