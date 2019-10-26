import six
from google.cloud import language
from google.cloud.language import enums
from google.cloud.language import types


def split_text(text):
    client = language.LanguageServiceClient()

    if isinstance(text, six.binary_type):
        text = text.decode('utf-8')

    # Instantiates a plain text document.
    document = types.Document(content=text,
                              type=enums.Document.Type.PLAIN_TEXT)

    print("analyze_syntax:", client.analyze_syntax(document))

    res = client.analyze_syntax(document)

    sentence_texts = [sentence.text.content for sentence in res.sentences]

    #     tokens = res.tokens
    #     for token in tokens:
    #         part_of_speech_tag = enums.PartOfSpeech.Tag(token.part_of_speech.tag)
    #         print(u'{}: {}'.format(part_of_speech_tag.name,
    #                                token.text.content))

    return sentence_texts
