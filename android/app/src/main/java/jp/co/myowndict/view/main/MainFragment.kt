package jp.co.myowndict.view.main

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startForegroundService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import androidx.viewpager2.widget.ViewPager2
import com.wada811.databinding.dataBinding
import dagger.android.support.DaggerFragment
import jp.co.myowndict.R
import jp.co.myowndict.databinding.FragmentMainBinding
import jp.co.myowndict.model.SpeechEvent
import jp.co.myowndict.speechrecognize.SpeechRecognizeService
import jp.co.myowndict.view.startAppSettingActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import permissions.dispatcher.*
import timber.log.Timber
import javax.inject.Inject

@RuntimePermissions
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
        startSpeechRecordingWithPermissionCheck()
        val adapter = MainFragmentPagerAdapter(this)
        binding.viewPager.adapter = adapter
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> adapter.onShowDictFragment()
                    1 -> adapter.onShowRecordingFragment()
                }
            }
        })

        viewModel.getSentances()
    }

    @NeedsPermission(Manifest.permission.RECORD_AUDIO)
    fun startSpeechRecording() {
        speechService = Intent(requireContext(), SpeechRecognizeService::class.java)
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

    @OnShowRationale(Manifest.permission.RECORD_AUDIO)
    fun showRationaleForContacts(request: PermissionRequest) {
        MaterialDialog(requireContext()).show {
            title(text = "マイクへのアクセスを許可してください")
            message(text = "録音を開始するには，マイクへのアクセスを許可する必要があります")
            positiveButton(text = "OK") { request.proceed() }
            lifecycleOwner(viewLifecycleOwner)
            cancelable(false)
        }
    }

    @OnPermissionDenied(Manifest.permission.RECORD_AUDIO)
    fun onContactsDenied() {
        MaterialDialog(requireContext()).show {
            message(text = "録音を開始するには，マイクへのアクセスを許可する必要があります")
            positiveButton(text = "OK")
            lifecycleOwner(viewLifecycleOwner)
            cancelable(false)
        }
    }

    @OnNeverAskAgain(Manifest.permission.RECORD_AUDIO)
    fun onContactsNeverAskAgain() {
        MaterialDialog(requireContext()).show {
            message(text = "録音を開始するには，マイクへのアクセスを許可する必要があります。設定画面を開きますか？")
            positiveButton(text = "OK") {
                startAppSettingActivity()
            }
            negativeButton(text = "戻る")
            lifecycleOwner(viewLifecycleOwner)
            cancelable(false)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
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
    fun onMessageRecieveEvent(event: SpeechEvent) {
        when (event) {
            is SpeechEvent.OnPartialResult -> event.partialText
            is SpeechEvent.OnResult -> event.text
        }

        Timber.d(event.toString())
    }

    class MainFragmentPagerAdapter(
        parentFragment: Fragment
    ) : FragmentStateAdapter(parentFragment) {
        private val fragments = listOf(DictFragment(), RecordingFragment())

        override fun createFragment(position: Int): Fragment = fragments[position]

        override fun getItemCount(): Int = fragments.size

        fun onShowDictFragment() {
            fragments.forEach { frag ->
                when (frag) {
                    is DictFragment -> frag.startTagAnimation()
                    is RecordingFragment -> frag.hideTag()
                }
            }
        }

        fun onShowRecordingFragment() {
            fragments.forEach { frag ->
                when (frag) {
                    is DictFragment -> frag.hideTag()
                    is RecordingFragment -> frag.startTagAnimation()
                }
            }
        }
    }
}
