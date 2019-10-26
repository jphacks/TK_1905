package jp.co.myowndict

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class ActivityModule {
    @ContributesAndroidInjector(
        modules = [
        ]
    )
    internal abstract fun contributeMainActivity(): MainActivity
}
