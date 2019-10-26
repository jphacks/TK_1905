from django.db import models


class Sentence(models.Model):
    text = models.ForeignKey("Text", on_delete=models.CASCADE)
    text_jp = models.TextField()
    text_en = models.TextField()

    created_at = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return f"{self.__class__.__name__}({self.text_jp})"
