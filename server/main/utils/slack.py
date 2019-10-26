import slackweb

from django.conf import settings

slack = slackweb.Slack(url=settings.SLACK_WEBHOOK_URL)


def slack_notify(text):
    try:
        slack.notify(text=text)
    except Exception as e:
        print(e)
