from django.db import models


class UserText(models.Model):
    user = models.ForeignKey("User", on_delete=models.CASCADE)
    text = models.ForeignKey("Text", on_delete=models.CASCADE)

    created_at = models.DateTimeField(auto_now_add=True)
