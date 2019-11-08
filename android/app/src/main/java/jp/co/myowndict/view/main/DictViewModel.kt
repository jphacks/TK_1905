package jp.co.myowndict.view.main

import androidx.core.text.HtmlCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import jp.co.myowndict.data.Repository
import jp.co.myowndict.extensions.launchWithProgress
import jp.co.myowndict.extensions.notify
import jp.co.myowndict.model.Result
import jp.co.myowndict.model.Sentence
import jp.co.myowndict.view.ApiFragmentViewModel
import timber.log.Timber
import javax.inject.Inject

class DictViewModel @Inject constructor(
    private val repository: Repository
) : ApiFragmentViewModel() {
    val sentences: LiveData<List<Sentence>>
        get() = sentencesLiveData
    val editEvent: LiveData<Unit>
        get() = _editEvent
    val deleteEvent: LiveData<Unit>
        get() = _deleteEvent


    private val sentencesLiveData: MutableLiveData<List<Sentence>> = MutableLiveData()
    private val _editEvent = LiveEvent<Unit>()
    private val _deleteEvent = LiveEvent<Unit>()

    fun getSentences() {
        viewModelScope.launchWithProgress(inProgressLiveData) {
            when (val result = repository.getSentences()) {
                is Result.Success -> sentencesLiveData.value =
                    result.data.sentences.map {
                        it.copy(
                            translatedContent = HtmlCompat.fromHtml(
                                it.translatedContent,
                                HtmlCompat.FROM_HTML_MODE_COMPACT
                            ).toString()
                        )
                    }
                is Result.Error -> Timber.e("Failed to fetch sentences")
            }
        }
    }

    fun editSentence(sentence: Sentence, updatedContentJp: String) {
        viewModelScope.launchWithProgress(inProgressLiveData) {
            when (val result = repository.editSentence(sentence.contentJp, updatedContentJp)) {
                is Result.Success -> {
                    _editEvent.notify()
                    sentencesLiveData.value = sentencesLiveData.value
                        ?.toMutableList()
                        ?.apply {
                            val index = indexOf(sentence)
                            removeAt(index)
                            add(index, result.data)
                        }
                }
                is Result.Error -> Timber.e("Failed to edit sentence")
            }
        }
    }

    fun deleteSentence(sentence: Sentence) {
        viewModelScope.launchWithProgress(inProgressLiveData) {
            when (repository.deleteSentence(sentence.contentJp)) {
                is Result.Success -> {
                    _deleteEvent.notify()
                    sentencesLiveData.value = sentencesLiveData.value
                        ?.toMutableList()
                        ?.apply { remove(sentence) }
                }
                is Result.Error -> Timber.e("Failed to delete sentence")
            }
        }
    }
}
