package jp.co.myowndict.view.splash

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.wada811.databinding.dataBinding
import dagger.android.support.DaggerFragment
import jp.co.myowndict.GlideApp
import jp.co.myowndict.R
import jp.co.myowndict.databinding.FragmentSplashBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


class SplashFragment : DaggerFragment(), CoroutineScope {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: SplashViewModel by viewModels { viewModelFactory }

    private val binding by dataBinding<FragmentSplashBinding>(R.layout.fragment_splash)

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private var gifDone = false
    private var entryDone = false

    private val gifListener = object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            if (resource is GifDrawable) {
                resource.setLoopCount(1)
                resource.registerAnimationCallback(object :
                    Animatable2Compat.AnimationCallback() {
                    override fun onAnimationEnd(drawable: Drawable?) {
                        gifDone = true
                        navigateIfWaitingHasDone()
                    }
                })
            }
            return false
        }
    }

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
        GlideApp.with(requireContext())
            .load(R.drawable.logo_animation)
            .addListener(gifListener)
            .into(binding.logoImageView)
    }

    private fun navigateIfWaitingHasDone() {
        if (gifDone && entryDone) {
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
