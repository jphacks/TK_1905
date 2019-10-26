package jp.co.myowndict

import dagger.Module
import dagger.android.ContributesAndroidInjector
import jp.co.myowndict.view.MainActivity

@Module
internal abstract class ActivityModule {
    @ContributesAndroidInjector(
        modules = [
        ]
    )
    internal abstract fun contributeMainActivity(): MainActivity
}
