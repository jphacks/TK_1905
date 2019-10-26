package jp.co.myowndict.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.co.myowndict.data.Repository
import jp.co.myowndict.extensions.combineLatest
import jp.co.myowndict.extensions.launchWithProgress
import jp.co.myowndict.model.Result
import jp.co.myowndict.view.ApiFragmentViewModel
import timber.log.Timber
import javax.inject.Inject

class RecordingViewModel @Inject constructor(
    private val repository: Repository
) : ApiFragmentViewModel() {
    private val partialResultLiveData: MutableLiveData<String> = MutableLiveData("")
    private val resultLiveData: MutableLiveData<String> = MutableLiveData("")

    val partialResult: LiveData<String>
        get() = partialResultLiveData
    val result: LiveData<String>
        get() = resultLiveData
    val recognizedSentence: LiveData<String> =
        partialResult.combineLatest(result) { partial, result ->
            "$result\n$partial"
        }

    fun sendSpeechText(text: String) {
        viewModelScope.launchWithProgress(inProgressLiveData) {
            when (val result = repository.sendText(text)) {
                is Result.Success -> Timber.d("Sent content -> $text")
                is Result.Error -> Timber.e("Failed to send content -> $text")
            }
        }
    }

    fun updatePartialResult(text: String) {
        if (text.isNotEmpty()) partialResultLiveData.value = text
    }

    fun clearPartialResult() {
        partialResultLiveData.value = ""
    }

    fun addResult(text: String) {
        resultLiveData.value = "${resultLiveData.value}\n$text"
    }
}
