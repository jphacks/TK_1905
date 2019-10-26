package jp.co.myowndict.extensions

import androidx.lifecycle.MutableLiveData
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun CoroutineScope.launchWithProgress(
    progressLiveData: MutableLiveData<Boolean>,
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job {
    return this.launch(context, start) {
        progressLiveData.value = true
        block.invoke(this)
        progressLiveData.value = false
    }
}
