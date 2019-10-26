package jp.co.myowndict.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Sentence(
    @Json(name = "content_jp") val contentJp: String,
    @Json(name = "content_en") val contentEn: String
)
