package jp.co.myowndict.view.main

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startForegroundService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wada811.databinding.dataBinding
import dagger.android.support.DaggerFragment
import jp.co.myowndict.R
import jp.co.myowndict.databinding.FragmentMainBinding
import jp.co.myowndict.speechrecognize.SpeechRecognizeService
import timber.log.Timber
import javax.inject.Inject

class MainFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: MainViewModel by viewModels { viewModelFactory }
    private lateinit var speechService: Intent

    private val binding by dataBinding<FragmentMainBinding>(R.layout.fragment_main)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewPager.adapter = MainFragmentPagerAdapter(this)

        startSpeechRecording()
    }

    private fun startSpeechRecording() {
        speechService = Intent(requireContext(), SpeechRecognizeService::class.java)
        speechService.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        speechService.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        if (SpeechRecognizeService.isRunning.not()) {
            startForegroundService(requireContext(), speechService)
        }
    }

    private fun stopSpeechRecording() {
        try {
            requireActivity().stopService(speechService)
        } catch (e: Exception) {
            Timber.d(e)
        }
    }

    class MainFragmentPagerAdapter(
        parentFragment: Fragment
    ) : FragmentStateAdapter(parentFragment) {
        private val fragments = listOf(DictFragment(), RecordingFragment())

        override fun createFragment(position: Int): Fragment = fragments[position]

        override fun getItemCount(): Int = fragments.size
    }
}
