package jp.co.myowndict.view.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import jp.co.myowndict.view.main.DictFragment
import jp.co.myowndict.view.main.MainFragment
import jp.co.myowndict.view.main.RecordingFragment
import jp.co.myowndict.view.splash.SplashFragment

@Suppress("unused")
@Module
internal abstract class FragmentModule {
    @ContributesAndroidInjector
    internal abstract fun contributeSplashFragment(): SplashFragment

    @ContributesAndroidInjector
    internal abstract fun contributeMainFragment(): MainFragment

    @ContributesAndroidInjector
    internal abstract fun contributeDictFragment(): DictFragment

    @ContributesAndroidInjector
    internal abstract fun contributeRecordingFragment(): RecordingFragment
}
