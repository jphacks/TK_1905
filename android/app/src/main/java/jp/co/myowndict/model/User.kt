package jp.co.myowndict.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    val name: String,
    @Json(name = "icon_base64") val iconBase64: String
)
