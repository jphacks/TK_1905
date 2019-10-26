from django.db import models


class Text(models.Model):
    user = models.ForeignKey("User", on_delete=models.CASCADE)
    text = models.TextField()

    created_at = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return f"{self.__class__.__name__}({self.name})"