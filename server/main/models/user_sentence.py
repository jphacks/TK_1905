from django.db import models


class UserSentence(models.Model):
    text = models.ForeignKey("UserText", on_delete=models.CASCADE)
    sentence = models.ForeignKey("Sentence", on_delete=models.CASCADE)

    created_at = models.DateTimeField(auto_now_add=True)
