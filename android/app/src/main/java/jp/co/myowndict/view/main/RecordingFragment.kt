package jp.co.myowndict.view.main

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.wada811.databinding.dataBinding
import dagger.android.support.DaggerFragment
import jp.co.myowndict.R
import jp.co.myowndict.databinding.FragmentRecordingBinding
import jp.co.myowndict.model.SpeechEvent
import jp.co.myowndict.speechrecognize.SpeechRecognizeService
import jp.co.myowndict.view.MainViewModel
import jp.co.myowndict.view.getNavigationBarSize
import jp.co.myowndict.view.getStatusBarSize
import jp.co.myowndict.view.updateMargins
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber
import javax.inject.Inject

class RecordingFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val mainViewModel: MainViewModel by activityViewModels { viewModelFactory }
    private val viewModel: RecordingViewModel by viewModels { viewModelFactory }
    private val binding by dataBinding<FragmentRecordingBinding>(R.layout.fragment_recording)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    private fun initLayoutMargins() {
        val statusBarSize = getStatusBarSize()
        binding.titleLabel.updateMargins(
            binding.titleLabel.marginLeft,
            binding.titleLabel.marginRight,
            binding.titleLabel.marginTop + statusBarSize,
            binding.titleLabel.marginBottom
        )
        val navigationBarSize = getNavigationBarSize()
        binding.slideBarImage.updateMargins(
            binding.slideBarImage.marginLeft,
            binding.slideBarImage.marginRight,
            binding.slideBarImage.marginTop,
            navigationBarSize
        )
        binding.fab.updateMargins(
            binding.fab.marginLeft,
            binding.fab.marginRight,
            binding.fab.marginTop,
            binding.fab.marginBottom + navigationBarSize
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initLayoutMargins()
        binding.fab.setOnClickListener { mainViewModel.stopRecording() }
        binding.also {
            it.viewModel = viewModel
        }
    }

    override fun onResume() {
        super.onResume()
        // TODO: 雑実装。後で修正
        mainViewModel.startRecording()
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
        SpeechRecognizeService.isBackground = false
    }

    override fun onStop() {
        super.onStop()
        SpeechRecognizeService.isBackground = true
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    @Suppress("unused")
    fun onMessageReceiveEvent(event: SpeechEvent) {
        EventBus.getDefault().removeStickyEvent(event)
        binding.textScrollView.post {
            binding.textScrollView.fullScroll(View.FOCUS_DOWN)
        }
        when (event) {
            is SpeechEvent.OnPartialResult -> viewModel.updatePartialResult(event.partialText)
            is SpeechEvent.OnResult -> {
                viewModel.clearPartialResult()
                viewModel.addResult(event.text)
                viewModel.sendSpeechText(event.text)
            }
            is SpeechEvent.OnIgnored -> {
                // 信頼度によって棄却された場合の処理
                viewModel.clearPartialResult()
                viewModel.addResult(event.text)
            }
        }

        Timber.d(event.toString())
    }
}
