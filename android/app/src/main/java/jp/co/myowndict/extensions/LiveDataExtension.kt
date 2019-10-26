package jp.co.myowndict.extensions

import androidx.lifecycle.*
import com.hadilq.liveevent.LiveEvent

fun <T> LiveData<T>.observeNonNull(owner: LifecycleOwner, observer: (T) -> Unit) =
    observe(owner, Observer<T> { if (it != null) observer(it) })

fun <T> MutableLiveData<T>.setValueIfNew(newValue: T) {
    if (this.value != newValue) value = newValue
}

fun <T> MutableLiveData<T>.postValueIfNew(newValue: T) {
    if (this.value != newValue) postValue(newValue)
}

fun LiveEvent<Unit>.notify() {
    value = Unit
}

fun <T1, T2, S> LiveData<T1>.combineLatest(source: LiveData<T2>, func: (T1, T2) -> S): LiveData<S> {
    val result = MediatorLiveData<S>()
    fun setValue() = value?.let { v1 -> source.value?.let { v2 -> result.value = func(v1, v2) } }
    result.addSource(this) { setValue() }
    result.addSource(source) { setValue() }
    return result
}