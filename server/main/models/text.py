from django.db import models


class Text(models.Model):
    content = models.TextField(primary_key=True)
    sentences = models.ManyToManyField("Sentence")

    created_at = models.DateTimeField(auto_now_add=True)
