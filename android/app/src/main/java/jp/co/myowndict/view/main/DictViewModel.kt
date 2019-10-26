package jp.co.myowndict.view.main

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
    private val sentencesLiveData: MutableLiveData<List<Sentence>> = MutableLiveData()
    private val _deleteEvent = LiveEvent<Unit>()
    val sentences: LiveData<List<Sentence>>
        get() = sentencesLiveData
    val deleteEvent: LiveData<Unit>
        get() = _deleteEvent

    fun getSentences() {
        viewModelScope.launchWithProgress(inProgressLiveData) {
            when (val result = repository.getSentences()) {
                is Result.Success -> sentencesLiveData.value = result.data.sentences
                is Result.Error -> Timber.e("Failed to fetch sentences")
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
