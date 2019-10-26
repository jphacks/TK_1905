from google.cloud import translate as google_translate


def translate(text, target='en'):
    translate_client = google_translate.Client()

    translation = translate_client.translate(text, target_language=target)
    print(translation)

    return translation['translatedText']
