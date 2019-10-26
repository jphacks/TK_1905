package jp.co.myowndict.model

class Token {
    companion object {
        private var token: String? = null

        fun get(): String? = token
        fun put(value: String) {
            token = value
        }
    }
}
