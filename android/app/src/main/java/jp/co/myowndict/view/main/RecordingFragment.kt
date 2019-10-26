package jp.co.myowndict.view.main

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.wada811.databinding.dataBinding
import dagger.android.support.DaggerFragment
import jp.co.myowndict.R
import jp.co.myowndict.databinding.FragmentRecordingBinding
import jp.co.myowndict.model.SpeechEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber
import javax.inject.Inject

class RecordingFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: RecordingViewModel by viewModels { viewModelFactory }
    private val binding by dataBinding<FragmentRecordingBinding>(R.layout.fragment_recording)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }

    fun startTagAnimation() {
        if (!isAdded) return
        ObjectAnimator.ofFloat(binding.slideBar, "x", -binding.slideBar.width.toFloat(), 0f).apply {
            duration = 300
            start()
        }
    }

    fun hideTag() {
        if (!isAdded) return
        binding.slideBar.x = -binding.slideBar.width.toFloat()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @Suppress("unused")
    fun onMessageReceiveEvent(event: SpeechEvent) {
        when (event) {
            is SpeechEvent.OnPartialResult -> viewModel.updatePartialResult(event.partialText)
            is SpeechEvent.OnResult -> viewModel.addResult(event.text)
        }

        Timber.d(event.toString())
    }

}
