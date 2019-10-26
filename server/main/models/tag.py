from django.db import models


class Tag(models.Model):
    name = models.CharField(max_length=63)

    created_at = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return f"{self.__class__.__name__}({self.name})"
