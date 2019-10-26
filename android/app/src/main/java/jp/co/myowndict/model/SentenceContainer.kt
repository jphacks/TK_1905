package jp.co.myowndict.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SentenceContainer(
    @Json(name = "results") val sentences: List<Sentence>
)
