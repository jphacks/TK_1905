package jp.co.myowndict.speechrecognize

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import dagger.android.DaggerService
import jp.co.myowndict.MyApplication
import jp.co.myowndict.R
import jp.co.myowndict.model.SpeechEvent
import jp.co.myowndict.view.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.greenrobot.eventbus.EventBus
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class SpeechRecognizeService : DaggerService(), CoroutineScope {
    private var speechRecognizer: SpeechRecognizer? = null
    private var streamVolume: Int = 0
    private lateinit var audioManager: AudioManager

    private val ignoredHolder = mutableListOf<SpeechEvent.OnIgnored>()
    private val resultHolder = mutableListOf<SpeechEvent.OnResult>()

    @Inject
    lateinit var application: MyApplication
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()

        val notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationWithUpperOreo()
        } else {
            createNotificationWithDownerOreo()
        }

        startForeground(1, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationWithUpperOreo(): Notification {
        val channelId = application.getString(R.string.notification_channel_id)
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle(application.getString(R.string.notification_content_title))
            .setContentText(application.getString(R.string.notification_content_text))
            .setSmallIcon(R.drawable.ic_recme_notifycation)
            .setContentIntent(createPendingIntent())
            .build()
    }

    private fun createNotificationWithDownerOreo(): Notification {
        return NotificationCompat.Builder(this)
            .setContentTitle(application.getString(R.string.notification_content_title))
            .setContentText(application.getString(R.string.notification_content_text))
            .setSmallIcon(R.drawable.ic_recme_notifycation)
            .setContentIntent(createPendingIntent())
            .build()
    }

    private fun createPendingIntent() = PendingIntent.getActivity(
        application.applicationContext,
        PUSH_REQUEST_CODE,
        Intent(application.applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        },
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.d("service is running")
        isRunning = true

        startListening()

        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        streamVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        return START_STICKY
    }

    private fun startListening() {
        try {
            if (speechRecognizer == null) {
                speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
                if (!SpeechRecognizer.isRecognitionAvailable(applicationContext)) {
                    Toast.makeText(
                        applicationContext, "音声認識が使えません",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                }
                speechRecognizer!!.setRecognitionListener(SpeechRecognizeListener())
            }
            // インテントの作成
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).also {
                // 言語モデル指定
                it.putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH
                )
                it.putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE,
                    "ja_JP"
                )
                it.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
                it.putExtra(
                    RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS,
                    1000
                )
                it.putExtra(
                    RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS,
                    1000
                )
            }

            speechRecognizer!!.startListening(intent)
        } catch (ex: Exception) {
            Toast.makeText(
                applicationContext, "startListening()でエラーが起こりました",
                Toast.LENGTH_LONG
            ).show()
            return
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false

        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, streamVolume, 0)
        stopListening()
        stopSelf()
    }

    private fun stopListening() {
        if (speechRecognizer != null) speechRecognizer?.destroy()
        speechRecognizer = null
    }

    private fun restartListeningService() {
        stopListening()
        startListening()
    }

    inner class SpeechRecognizeListener : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {
            Timber.d("話してください")
        }

        override fun onRmsChanged(rmsdB: Float) {
        }

        override fun onBufferReceived(buffer: ByteArray?) {
        }

        override fun onPartialResults(partialResults: Bundle?) {
            partialResults ?: return
            partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                ?.takeIf { it.isNotEmpty() }?.let {
                    EventBus.getDefault().postSticky(SpeechEvent.OnPartialResult(it.first()))
                }
        }

        override fun onEvent(eventType: Int, params: Bundle?) {
        }

        override fun onBeginningOfSpeech() {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0)
        }

        override fun onEndOfSpeech() {
        }

        override fun onError(error: Int) {
            var reason = ""
            when (error) {
                // Audio recording error
                SpeechRecognizer.ERROR_AUDIO -> reason = "ERROR_AUDIO"
                // Other client side errors
                SpeechRecognizer.ERROR_CLIENT -> reason = "ERROR_CLIENT"
                // Insufficient permissions
                SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> reason =
                    "ERROR_INSUFFICIENT_PERMISSIONS"
                // 	Other network related errors
                SpeechRecognizer.ERROR_NETWORK -> reason = "ERROR_NETWORK"
                // Network operation timed out
                SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> reason = "ERROR_NETWORK_TIMEOUT"
                // No recognition result matched
                SpeechRecognizer.ERROR_NO_MATCH -> reason = "ERROR_NO_MATCH"
                // RecognitionService busy
                SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> reason = "ERROR_RECOGNIZER_BUSY"
                // Server sends error status
                SpeechRecognizer.ERROR_SERVER -> reason = "ERROR_SERVER"
                // No speech input
                SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> reason = "ERROR_SPEECH_TIMEOUT"
            }/* ネットワーク接続をチェックする処理をここに入れる *//* ネットワーク接続をチェックをする処理をここに入れる */
            Timber.d(reason)
            restartListeningService()
        }

        override fun onResults(results: Bundle?) {
            results ?: return
            val candidates = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            val confidences = results.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES)
            val s: String? = candidates?.first()
            val confidenceScore = confidences?.first()

            s?.let {
                Timber.d(it)
                Timber.d("$confidenceScore")
                if (confidenceScore!! < MIN_CONFIDENCE_SCORE) {
                    Timber.w("Result was ignored by low confidence score.")
                    postIgnored(it)
                } else {
                    postResult(it)
                }
            }

            // トーストで結果を表示
            Timber.d(s)
            restartListeningService()
        }

        private fun postIgnored(text: String) {
            val eventBus = EventBus.getDefault()
            val ignored = SpeechEvent.OnIgnored(text)
            if (isBackground) {
                ignoredHolder.add(ignored)
            } else {
                ignoredHolder.forEach { i -> eventBus.postSticky(i) }
                ignoredHolder.clear()
                eventBus.postSticky(ignored)
            }
        }

        private fun postResult(text: String) {
            val eventBus = EventBus.getDefault()
            val result = SpeechEvent.OnResult(text)
            if (isBackground) {
                resultHolder.add(result)
            } else {
                resultHolder.forEach { r -> eventBus.postSticky(r) }
                resultHolder.clear()
                eventBus.postSticky(result)
            }
        }
    }

    companion object {
        private const val MIN_CONFIDENCE_SCORE = 0.85
        private const val PUSH_REQUEST_CODE = 1000

        var isRunning: Boolean = false
            private set

        // 良くない実装だが諦める
        var isBackground: Boolean = false
    }
}
