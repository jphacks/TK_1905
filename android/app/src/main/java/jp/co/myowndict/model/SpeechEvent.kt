package jp.co.myowndict.model

sealed class SpeechEvent {
    data class OnResult(
        val text: String
    ) : SpeechEvent()

    data class OnIgnored(
        val text: String
    ) : SpeechEvent()

    data class OnPartialResult(
        val partialText: String
    ) : SpeechEvent()
}
