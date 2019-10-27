package jp.co.myowndict.view.quiz

import androidx.core.text.HtmlCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.co.myowndict.data.Repository
import jp.co.myowndict.extensions.launchWithProgress
import jp.co.myowndict.model.Result
import jp.co.myowndict.model.Sentence
import jp.co.myowndict.view.ApiFragmentViewModel
import timber.log.Timber
import javax.inject.Inject

class QuizViewModel @Inject constructor(
    private val repository: Repository
) : ApiFragmentViewModel() {
    private val sentencesLiveData: MutableLiveData<List<Sentence>> = MutableLiveData()

    val sentences: LiveData<List<Sentence>>
        get() = sentencesLiveData

    fun getSentences() {
        viewModelScope.launchWithProgress(inProgressLiveData) {
            when (val result = repository.getSentences()) {
                is Result.Success -> sentencesLiveData.value =
                    result.data.sentences.map {
                        it.copy(
                            contentEn = HtmlCompat.fromHtml(
                                it.contentEn,
                                HtmlCompat.FROM_HTML_MODE_COMPACT
                            ).toString()
                        )
                    }
                is Result.Error -> Timber.e("Failed to fetch sentences")
            }
        }
    }
}
