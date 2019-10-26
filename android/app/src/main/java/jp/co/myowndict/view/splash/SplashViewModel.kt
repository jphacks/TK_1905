package jp.co.myowndict.view.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import jp.co.myowndict.extensions.notify
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashViewModel @Inject constructor() : ViewModel() {

    val authenticated
        get() = _authenticated

    private val _authenticated = LiveEvent<Unit>()

    fun authenticate() = viewModelScope.launch {
        delay(2000)
        _authenticated.notify()
    }
}