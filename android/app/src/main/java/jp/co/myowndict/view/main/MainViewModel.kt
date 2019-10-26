package jp.co.myowndict.view.main

import androidx.lifecycle.viewModelScope
import jp.co.myowndict.data.Repository
import jp.co.myowndict.extensions.launchWithProgress
import jp.co.myowndict.model.Result
import jp.co.myowndict.view.ApiFragmentViewModel
import timber.log.Timber
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val repository: Repository
) : ApiFragmentViewModel() {
    fun sendSpeechText(text: String) {
        viewModelScope.launchWithProgress(inProgressLiveData) {
            when (val result = repository.sendText(text)) {
                is Result.Success -> Timber.d("Sent text -> $text")
                is Result.Error -> Timber.e("Failed to send text -> $text")
            }
        }
    }
}
