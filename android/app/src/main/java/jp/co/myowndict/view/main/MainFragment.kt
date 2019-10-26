package jp.co.myowndict.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.wada811.databinding.dataBinding
import dagger.android.support.DaggerFragment
import jp.co.myowndict.R
import jp.co.myowndict.databinding.FragmentMainBinding
import jp.co.myowndict.extensions.observeNonNull
import jp.co.myowndict.view.MainViewModel
import javax.inject.Inject

class MainFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val mainViewModel: MainViewModel by activityViewModels { viewModelFactory }

    private val binding by dataBinding<FragmentMainBinding>(R.layout.fragment_main)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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

        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            v.updatePadding(bottom = v.paddingBottom + insets.systemWindowInsetBottom)
            insets
        }

        observe()
    }

    private fun observe() {
        mainViewModel.isRunning.observeNonNull(viewLifecycleOwner) { isRunning ->
            if (!isRunning) binding.viewPager.setCurrentItem(0, true)
        }
        mainViewModel.showQuizEvent.observeNonNull(viewLifecycleOwner) {
            showQuizFragment()
        }
    }

    private fun showQuizFragment() {
        findNavController().navigate(MainFragmentDirections.actionMainToQuiz())
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
