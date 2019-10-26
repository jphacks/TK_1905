import six
from google.cloud import language
from google.cloud.language import enums
from google.cloud.language import types


def _split_text_with_rules(tokens,
                           eof_roles=["です", "ね"],
                           eof_words=["か", "て", "たら"]):
    texts = []

    sentence = ""
    end_flag = False
    for token in tokens:
        content = token.text.content
        part_of_speech_tag = enums.PartOfSpeech.Tag(token.part_of_speech.tag)

        if end_flag and part_of_speech_tag.name == "VERB":
            end_flag = True
        elif end_flag and part_of_speech_tag.name != "PRT":
            texts.append(sentence)
            sentence = ""
            end_flag = False

        # print(part_of_speech_tag.name, content,
        #       token.dependency_edge.head_token_index)
        sentence += content
        for eof_role in eof_roles:
            if eof_role in content:
                end_flag = True
        for eof_word in eof_words:
            if eof_word == content:
                end_flag = True

    texts.append(sentence)

    return texts


def split_text(text):
    client = language.LanguageServiceClient()

    if isinstance(text, six.binary_type):
        text = text.decode('utf-8')

    # Instantiates a plain text document.
    document = types.Document(content=text,
                              type=enums.Document.Type.PLAIN_TEXT)

    res = client.analyze_syntax(document)
    # print("analyze_syntax:", res)

    # sentence_texts = []
    # for sentence in res.sentences:
    #     text_content = sentence.text.content
    #     print(text_content)
    #     sentence_texts.append(text_content)

    # tokens = res.tokens
    # for token in tokens:
    #     # print(token)
    #     part_of_speech_tag = enums.PartOfSpeech.Tag(token.part_of_speech.tag)
    #     print(part_of_speech_tag.name, token.text.content,
    #           token.dependency_edge.head_token_index)

    return _split_text_with_rules(res.tokens)
