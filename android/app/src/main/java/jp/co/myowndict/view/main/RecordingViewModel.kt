package jp.co.myowndict.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import jp.co.myowndict.model.Sentence
import javax.inject.Inject

class RecordingViewModel @Inject constructor() : ViewModel() {
    private val sentencesLiveData: MutableLiveData<List<Sentence>> = MutableLiveData()
    val sentences: LiveData<List<Sentence>>
        get() = sentencesLiveData

    private val partialResultLiveData: MutableLiveData<String> = MutableLiveData("")
    private val resultLiveData: MutableLiveData<String> = MutableLiveData("")

    fun updatePartialResult(text: String) {
        partialResultLiveData.value = text
    }

    fun addResult(text: String) {
        resultLiveData.value = "${resultLiveData.value}\n$text"
    }
}
