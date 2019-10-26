package jp.co.myowndict.view

import dagger.Module
import dagger.android.ContributesAndroidInjector
import jp.co.myowndict.view.main.MainFragment
import jp.co.myowndict.view.splash.SplashFragment

@Suppress("unused")
@Module
internal abstract class FragmentModule {
    @ContributesAndroidInjector
    internal abstract fun contributeSplashFragment(): SplashFragment

    @ContributesAndroidInjector
    internal abstract fun contributeMainFragment(): MainFragment
}