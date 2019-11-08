package jp.co.myowndict

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import jp.co.myowndict.di.DaggerAppComponent
import timber.log.Timber

class MyApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder()
            .application(this)
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initNotificationChannel() {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val name = getString(R.string.notification_channel_name)
        val id = getString(R.string.notification_channel_id)
        val notifyDescription = getString(R.string.notification_channel_description)

        if (manager.getNotificationChannel(id) == null) {
            val mChannel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH)
            mChannel.apply {
                description = notifyDescription
            }
            manager.createNotificationChannel(mChannel)
        }
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            // デバッグのときのみ，Hyperion-Crashを使うためにFirebaseAppの自動初期化を無効化しているため，ここで手動初期化する
            FirebaseApp.initializeApp(this)
            Timber.plant(Timber.DebugTree())
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            initNotificationChannel()
        }

        FirebaseAnalytics.getInstance(this)
    }
}
