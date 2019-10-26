package jp.co.myowndict.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CancellationException
import timber.log.Timber

/**
 * ApiFragmentViewModel
 * Base view model for fragments.
 * Do not instantiate this class.
 */
abstract class ApiFragmentViewModel : ViewModel() {
    protected val apiErrorLiveData: MutableLiveData<Throwable> = MutableLiveData()
    protected val inProgressLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply { value = false }

    val apiError: LiveData<Throwable>
        get() = apiErrorLiveData
    val inProgress: LiveData<Boolean>
        get() = inProgressLiveData

    override fun onCleared() {
        super.onCleared()
        inProgressLiveData.value = false
    }
}
