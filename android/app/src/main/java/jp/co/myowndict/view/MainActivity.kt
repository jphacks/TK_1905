package jp.co.myowndict.view

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import dagger.android.support.DaggerAppCompatActivity
import jp.co.myowndict.R
import jp.co.myowndict.extensions.observeNonNull
import jp.co.myowndict.speechrecognize.SpeechRecognizeService
import permissions.dispatcher.*
import timber.log.Timber
import javax.inject.Inject

@RuntimePermissions
class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: MainViewModel by viewModels { viewModelFactory }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        observe()
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    }

    @NeedsPermission(Manifest.permission.RECORD_AUDIO)
    fun startSpeechRecording() {
        val speechService = Intent(this, SpeechRecognizeService::class.java)
        ContextCompat.startForegroundService(this, speechService)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    private fun observe() {
        viewModel.isRunning.observeNonNull(this) {
            if (it) startSpeechRecordingWithPermissionCheck()
            else stopSpeechRecording()
        }
    }

    private fun stopSpeechRecording() {
        try {
            stopService(Intent(this, SpeechRecognizeService::class.java))
        } catch (e: Exception) {
            Timber.d(e)
        }
    }

    @OnShowRationale(Manifest.permission.RECORD_AUDIO)
    fun showRationaleForContacts(request: PermissionRequest) {
        MaterialDialog(this).show {
            title(text = "マイクへのアクセスを許可してください")
            message(text = "録音を開始するには，マイクへのアクセスを許可する必要があります")
            positiveButton(text = "OK") { request.proceed() }
            lifecycleOwner(this@MainActivity)
            cancelable(false)
        }
    }

    @OnPermissionDenied(Manifest.permission.RECORD_AUDIO)
    fun onContactsDenied() {
        MaterialDialog(this).show {
            message(text = "録音を開始するには，マイクへのアクセスを許可する必要があります")
            positiveButton(text = "OK")
            lifecycleOwner(this@MainActivity)
            cancelable(false)
        }
    }

    @OnNeverAskAgain(Manifest.permission.RECORD_AUDIO)
    fun onContactsNeverAskAgain() {
        MaterialDialog(this).show {
            message(text = "録音を開始するには，マイクへのアクセスを許可する必要があります。設定画面を開きますか？")
            positiveButton(text = "OK") {
                startAppSettingActivity()
            }
            negativeButton(text = "戻る")
            lifecycleOwner(this@MainActivity)
            cancelable(false)
        }
    }

    private fun startAppSettingActivity() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:${packageName}")
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}
