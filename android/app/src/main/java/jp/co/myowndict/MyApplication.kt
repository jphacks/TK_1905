package jp.co.myowndict

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

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            // デバッグのときのみ，Hyperion-Crashを使うためにFirebaseAppの自動初期化を無効化しているため，ここで手動初期化する
            FirebaseApp.initializeApp(this)
            Timber.plant(Timber.DebugTree())
        }

        FirebaseAnalytics.getInstance(this)
    }
}
