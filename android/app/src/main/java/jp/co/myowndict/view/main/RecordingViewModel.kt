package jp.co.myowndict.view.main

import android.text.*
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import jp.co.myowndict.MyApplication
import jp.co.myowndict.R
import jp.co.myowndict.data.Repository
import jp.co.myowndict.extensions.combineLatest
import jp.co.myowndict.extensions.launchWithProgress
import jp.co.myowndict.model.Result
import jp.co.myowndict.view.ApiFragmentViewModel
import timber.log.Timber
import javax.inject.Inject

class RecordingViewModel @Inject constructor(
    private val application: MyApplication,
    private val repository: Repository
) : ApiFragmentViewModel() {
    private val partialResultLiveData: MutableLiveData<Spanned> =
        MutableLiveData(SpannedString(""))
    private val resultLiveData: MutableLiveData<Spanned> = MutableLiveData(SpannedString(""))

    val partialResult: LiveData<Spanned>
        get() = partialResultLiveData
    val result: LiveData<Spanned>
        get() = resultLiveData
    val recognizedSentence: LiveData<Spanned> =
        partialResult.combineLatest(result) { partial, result -> concatSpannable(result, partial) }

    fun sendSpeechText(text: String) {
        viewModelScope.launchWithProgress(inProgressLiveData) {
            when (val result = repository.sendText(text)) {
                is Result.Success -> {
                    Timber.d("Sent content -> $text")
                    // 非同期処理の扱いはとりあえず考えない
//                    partialResult.value = partialResult.value.run {
//                        replace()
//                    }
                }
                is Result.Error -> Timber.e("Failed to send content -> $text")
            }
        }
    }

    fun updatePartialResult(text: String) {
        if (text.isNotEmpty()) partialResultLiveData.value = SpannedString("")
    }

    fun clearPartialResult() {
        partialResultLiveData.value = SpannedString("")
    }

    fun addResult(text: String) {
        resultLiveData.value = concatSpannable(
            resultLiveData.value ?: SpannedString(""),
            SpannedString(text)
        )
    }

    private fun concatSpannable(s1: Spanned, s2: Spanned): Spanned {
        val colorSpan = ForegroundColorSpan(
            ContextCompat.getColor(
                application,
                R.color.colorAccent
            )
        )
        val builder = SpannableStringBuilder(s1).apply {
            setSpan(colorSpan, 0, s1.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        }
        return TextUtils.concat(builder, "\n", s2) as Spanned
    }
}
