package jp.co.myowndict.model

sealed class SpeechEvent {
    data class OnResultEvent(
        val text: String
    ) : SpeechEvent()

    data class OnPartialResult(
        val partialText: String
    ) : SpeechEvent()
}
