package jp.co.myowndict.view.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import jp.co.myowndict.data.Repository
import jp.co.myowndict.extensions.notify
import kotlinx.coroutines.launch
import javax.inject.Inject
import jp.co.myowndict.model.Result

class SplashViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    fun signUp(): Pair<LiveEvent<Unit>, LiveEvent<Unit>> {
        val onSuccess = LiveEvent<Unit>()
        val onFailure = LiveEvent<Unit>()

        viewModelScope.launch {
            when (val result = repository.signUp()) {
                is Result.Success -> onSuccess.notify()
                is Result.Error -> onFailure.notify()
            }
        }

        return Pair(onSuccess, onFailure)
    }

    fun signIn(): Pair<LiveEvent<Unit>, LiveEvent<Unit>> {
        val onSuccess = LiveEvent<Unit>()
        val onFailure = LiveEvent<Unit>()

        viewModelScope.launch {
            when (val result = repository.signIn(repository.getUuid()!!)) {
                is Result.Success -> onSuccess.notify()
                is Result.Error -> onFailure.notify()
            }
        }

        return Pair(onSuccess, onFailure)
    }

    fun isNewUser(): Boolean = repository.getUuid() == null
}
