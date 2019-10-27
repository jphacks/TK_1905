package jp.co.myowndict.view.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.wada811.databinding.dataBinding
import dagger.android.support.DaggerFragment
import jp.co.myowndict.R
import jp.co.myowndict.databinding.FragmentSplashBinding
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class SplashFragment : DaggerFragment(), CoroutineScope {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: SplashViewModel by viewModels { viewModelFactory }

    private val binding by dataBinding<FragmentSplashBinding>(R.layout.fragment_splash)

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private var waitDone = false
    private var entryDone = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (viewModel.isNewUser()) {
            signUp()
        } else {
            signIn()
        }

        launch {
            delay(600)
            waitDone = true
            navigateIfWaitingHasDone()
        }
    }

    private fun navigateIfWaitingHasDone() {
        if (waitDone && entryDone) {
            findNavController().navigate(SplashFragmentDirections.actionSplashToMain())
        }
    }

    private fun signUp() {
        val (onSuccess, onFailure) = viewModel.signUp()

        onSuccess.observe(viewLifecycleOwner, Observer {
            entryDone = true
            navigateIfWaitingHasDone()
        })
        onFailure.observe(viewLifecycleOwner, Observer {
            MaterialDialog(requireContext()).show {
                title(text = "通信エラー")
                positiveButton(text = "リトライ") { signUp() }
            }
        })
    }

    private fun signIn() {
        val (onSuccess, onFailure) = viewModel.signIn()

        onSuccess.observe(viewLifecycleOwner, Observer {
            entryDone = true
            navigateIfWaitingHasDone()
        })
        onFailure.observe(viewLifecycleOwner, Observer {
            MaterialDialog(requireContext()).show {
                title(text = "通信エラー")
                positiveButton(text = "リトライ") { signIn() }
            }
        })
    }
}
