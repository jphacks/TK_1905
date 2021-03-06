package jp.co.myowndict.model

data class Token(
    val token: String
)

object TokenManager {
        private var token: String? = null

        fun get(): String? = token
        fun put(value: String) {
            token = value
        }
}
