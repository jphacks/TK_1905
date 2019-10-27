import MeCab
from gensim.models.doc2vec import Doc2Vec
import numpy as np

from .singleton import Singleton


def tokenize(text):
    wakati = MeCab.Tagger("-O wakati")
    wakati.parse("")
    return wakati.parse(text).strip().split()


class Wikipedia(Singleton):
    model = None

    def calc_vec(self, text):
        if self.model is None:
            self.model = Doc2Vec.load(
                "local/wikipedia/jawiki.doc2vec.dbow300d.model")
        return self.model.infer_vector(tokenize(text))


def cos_similarity(v1, v2):
    return np.dot(v1, v2) / (np.linalg.norm(v1) * np.linalg.norm(v2))


def doc2vec(text1, text2):
    wikipedia = Wikipedia.get_instance()
    vec1 = wikipedia.calc_vec(text1)
    vec2 = wikipedia.calc_vec(text2)

    return cos_similarity(vec1, vec2)
