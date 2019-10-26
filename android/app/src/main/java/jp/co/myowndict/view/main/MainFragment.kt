package jp.co.myowndict.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.wada811.databinding.dataBinding
import dagger.android.support.DaggerFragment
import jp.co.myowndict.R
import jp.co.myowndict.databinding.FragmentMainBinding
import jp.co.myowndict.view.splash.SplashViewModel
import javax.inject.Inject

class MainFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: MainViewModel by viewModels { viewModelFactory }

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
    }

    class MainFragmentPagerAdapter(
        parentFragment: Fragment
    ) : FragmentStateAdapter(parentFragment) {
        private val fragments = listOf(DictFragment(), RecordingFragment())

        override fun createFragment(position: Int): Fragment = fragments[position]

        override fun getItemCount(): Int = fragments.size
    }
}
