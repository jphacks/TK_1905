from django.db import models

TWEET_STATUS_CHOICES = ((0, '公開'), (1, '下書き'), (2, '非公開'))


class Tweet(models.Model):
    user = models.ForeignKey("User", on_delete=models.CASCADE)
    text = models.TextField()

    status = models.IntegerField(choices=TWEET_STATUS_CHOICES, default=0)
    tags = models.ManyToManyField("Tag")

    created_at = models.DateTimeField(auto_now_add=True)

    def status_display(self):
        return self.get_status_display()
