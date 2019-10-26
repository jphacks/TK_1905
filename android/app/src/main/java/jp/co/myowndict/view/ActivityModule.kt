package jp.co.myowndict.view

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import jp.co.myowndict.di.ViewModelKey
import jp.co.myowndict.view.splash.SplashViewModel

@Module
internal abstract class ActivityModule {
    @ContributesAndroidInjector(
        modules = [
            FragmentModule::class
        ]
    )
    internal abstract fun contributeMainActivity(): MainActivity


    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindSplashViewModel(viewModel: SplashViewModel): ViewModel
}
