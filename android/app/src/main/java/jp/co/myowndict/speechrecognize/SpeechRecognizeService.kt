package jp.co.myowndict.speechrecognize

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import timber.log.Timber

class SpeechRecognizeService : Service() {
    private var speechRecognizeListener: SpeechRecognizer? = null
    private lateinit var notification: Notification

    override fun onBind(intent: Intent?): IBinder? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationWithUpperOreo()
        } else {
            createNotificationWithDownerOreo()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationWithUpperOreo() {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val name = "通知のタイトル的情報を設定"
        val id = "casareal_foreground"
        val notifyDescription = "この通知の詳細情報を設定します"

        if (manager.getNotificationChannel(id) == null) {
            val mChannel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH)
            mChannel.apply {
                description = notifyDescription
            }
            manager.createNotificationChannel(mChannel)

        }

        notification = NotificationCompat.Builder(this, id)
            .setContentTitle("通知のタイトル")
            .setContentText("通知の内容")
            .build()
    }

    private fun createNotificationWithDownerOreo() {
         notification = NotificationCompat.Builder(this)
            .setContentTitle("通知のタイトル")
            .setContentText("通知の内容")
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Timber.d("service is running")
        isRunning = true

        startForeground(1, notification)

        Thread(Runnable {
            while (true) {
                Thread.sleep(1_000)
                Timber.d("test thread")
            }
        }).start()

        return START_STICKY
    }

    private fun startListening() {
        try {
            if (speechRecognizeListener == null) {
                speechRecognizeListener = SpeechRecognizer.createSpeechRecognizer(this)
                if (!SpeechRecognizer.isRecognitionAvailable(applicationContext)) {
                    Toast.makeText(
                        applicationContext, "音声認識が使えません",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                }
                speechRecognizeListener!!.setRecognitionListener(SpeechRecognizeListener())
            }
            // インテントの作成
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            // 言語モデル指定
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH
            )
            speechRecognizeListener!!.startListening(intent)
        } catch (ex: Exception) {
            Toast.makeText(
                applicationContext, "startListening()でエラーが起こりました",
                Toast.LENGTH_LONG
            ).show()
            return
        }
    }

    companion object {
        var isRunning: Boolean = false
    }
}
