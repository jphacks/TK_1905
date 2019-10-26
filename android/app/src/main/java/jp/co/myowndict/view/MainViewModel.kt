package jp.co.myowndict.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import jp.co.myowndict.extensions.setValueIfNew
import javax.inject.Inject

class MainViewModel @Inject constructor() : ViewModel() {
    val isRunning: LiveData<Boolean>
        get() = _isRunning

    private val _isRunning = MutableLiveData(false)

    fun startRecording() {
        _isRunning.setValueIfNew(true)
    }

    fun stopRecording() {
        _isRunning.setValueIfNew(false)
    }
}
