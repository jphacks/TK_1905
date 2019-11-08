package jp.co.myowndict.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Sentence(
    @Json(name = "content_jp") val contentJp: String,
    @Json(name = "translated_content") val translatedContent: String,
    @Json(name = "spoken_count") val spokenCount: Int
)
