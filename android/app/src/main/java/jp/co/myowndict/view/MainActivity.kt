package jp.co.myowndict.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import jp.co.myowndict.R
import jp.co.myowndict.speechrecognize.SpeechRecognizeService

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val speechService = Intent(application, SpeechRecognizeService::class.java)
        if (SpeechRecognizeService.isRunning.not()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(speechService)
            } else {
                startService(speechService)
            }
        }
    }
}
