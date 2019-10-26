package jp.co.myowndict.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlin.random.Random

@JsonClass(generateAdapter = true)
data class Sentence(
    @Json(name = "content_jp") val contentJp: String,
    @Json(name = "content_en") val contentEn: String,
    val count: Int = Random.nextInt() % 20   // 用途不明。とりあえずランダム値を設定
)
