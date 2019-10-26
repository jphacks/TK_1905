package jp.co.myowndict.speechrecognize

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class ServiceModule {
    @ContributesAndroidInjector
    internal abstract fun contributeSpeechRecognizeService(): SpeechRecognizeService
}
