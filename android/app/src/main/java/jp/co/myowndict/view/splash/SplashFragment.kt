package jp.co.myowndict.view.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.wada811.databinding.dataBinding
import dagger.android.support.DaggerFragment
import jp.co.myowndict.R
import jp.co.myowndict.databinding.FragmentSplashBinding
import jp.co.myowndict.extensions.observeNonNull
import javax.inject.Inject

class SplashFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: SplashViewModel by viewModels { viewModelFactory }

    private val binding by dataBinding<FragmentSplashBinding>(R.layout.fragment_splash)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observe()

        viewModel.authenticate()
    }

    private fun observe() {
        viewModel.authenticated.observeNonNull(viewLifecycleOwner) {
            findNavController().navigate(SplashFragmentDirections.actionSplashToMain())
        }
    }
}
