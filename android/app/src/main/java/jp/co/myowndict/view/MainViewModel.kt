package jp.co.myowndict.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import jp.co.myowndict.extensions.notify
import jp.co.myowndict.extensions.setValueIfNew
import jp.co.myowndict.speechrecognize.SpeechRecognizeService
import javax.inject.Inject

class MainViewModel @Inject constructor() : ViewModel() {
    val isRunning: LiveData<Boolean>
        get() = _isRunning
    val startRecordingEvent: LiveData<Unit>
        get() = _startRecordingEvent
    val stopRecordingEvent: LiveData<Unit>
        get() = _stopRecordingEvent
    val showQuizEvent: LiveData<Unit>
        get() = _showQuizEvent

    private val _isRunning = MutableLiveData<Boolean>()
    private val _startRecordingEvent = LiveEvent<Unit>()
    private val _stopRecordingEvent = LiveEvent<Unit>()
    private val _showQuizEvent = LiveEvent<Unit>()

    fun startRecording() {
        if (_isRunning.value != true) {
            _isRunning.value = true
            _startRecordingEvent.notify()
        }
    }

    fun stopRecording() {
        if (_isRunning.value == true) {
            _isRunning.value = false
            _stopRecordingEvent.notify()
        }
    }

    fun updateSpeechRecognizeServiceState() {
        _isRunning.setValueIfNew(SpeechRecognizeService.isRunning)
    }

    fun showQuiz() = _showQuizEvent.notify()
}
