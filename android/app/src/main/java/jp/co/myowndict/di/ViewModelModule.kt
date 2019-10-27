package jp.co.myowndict.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import jp.co.myowndict.view.main.DictViewModel
import jp.co.myowndict.view.main.RecordingViewModel
import jp.co.myowndict.view.quiz.QuizViewModel

/**
 * ViewModelModule
 * write provider functions to provide ViewModel or ViewModelFactory.
 */
@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(DictViewModel::class)
    abstract fun bindDictViewModel(viewModel: DictViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RecordingViewModel::class)
    abstract fun bindRecordingViewModel(viewModel: RecordingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(QuizViewModel::class)
    abstract fun bindQuizViewModel(viewModel: QuizViewModel): ViewModel
}
