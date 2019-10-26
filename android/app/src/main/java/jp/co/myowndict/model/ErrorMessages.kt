package jp.co.myowndict.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorMessages(
    @Json(name = "error_messages") val errorMessages: List<String>
)
