package jp.co.myowndict.view.main

import android.animation.ObjectAnimator
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.view.*
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.wada811.databinding.dataBinding
import dagger.android.support.DaggerFragment
import jp.co.myowndict.R
import jp.co.myowndict.databinding.FragmentDictBinding
import jp.co.myowndict.extensions.observeNonNull
import jp.co.myowndict.model.Sentence
import jp.co.myowndict.view.MainViewModel
import jp.co.myowndict.view.getNavigationBarSize
import jp.co.myowndict.view.getStatusBarSize
import jp.co.myowndict.view.updateMargins
import java.util.*
import javax.inject.Inject

class DictFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val mainViewModel: MainViewModel by activityViewModels { viewModelFactory }
    private val dictViewModel: DictViewModel by viewModels { viewModelFactory }
    private val binding by dataBinding<FragmentDictBinding>(R.layout.fragment_dict)
    private lateinit var adapter: SentenceAdapter
    private lateinit var tts: TextToSpeech

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tts = TextToSpeech(requireContext()) {
            when (it) {
                TextToSpeech.SUCCESS -> tts.language = Locale.US
                else -> Toast.makeText(
                    context,
                    R.string.tts_init_error_message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.viewModel = mainViewModel
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
        binding.micIconImageView.updateMargins(
            binding.micIconImageView.marginLeft,
            binding.micIconImageView.marginRight,
            binding.micIconImageView.marginTop,
            navigationBarSize
        )
        binding.recyclerView.updatePadding(bottom = binding.recyclerView.paddingBottom + navigationBarSize)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initLayoutMargins()
        adapter = SentenceAdapter(viewLifecycleOwner,
            onClick = { sentence -> speakSentence(sentence) },
            onLongClick = { sentence -> deleteSentence(sentence) }
        )
        binding.recyclerView.let { rv ->
            rv.adapter = adapter

            val controller = AnimationUtils.loadLayoutAnimation(
                context,
                R.anim.layout_animation_slide_from_left
            )
            rv.layoutAnimation = controller
        }
        binding.refreshLayout.setOnRefreshListener {
            dictViewModel.getSentences()
        }
        binding.quizFab.setOnClickListener { mainViewModel.showQuiz() }
        dictViewModel.getSentences()

        observe()
    }

    override fun onDestroy() {
        tts.shutdown()
        super.onDestroy()
    }

    private fun observe() {
        dictViewModel.sentences.observeNonNull(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.refreshLayout.isRefreshing = false
        }
        dictViewModel.deleteEvent.observeNonNull(viewLifecycleOwner) {
            // とりあえずToast
            Toast.makeText(context, "削除しました", Toast.LENGTH_SHORT).show()
        }
    }

    fun startTagAnimation() {
        if (!isAdded) return
        ObjectAnimator.ofFloat(
            binding.slideBar,
            "x",
            binding.root.width.toFloat(),
            binding.root.width.toFloat() - binding.slideBar.width
        ).apply {
            duration = 300
            start()
        }

        binding.recyclerView.startLayoutAnimation()
    }

    fun hideTag() {
        if (!isAdded) return
        binding.slideBar.x = binding.root.width.toFloat()
    }

    private fun speakSentence(sentence: Sentence) {
        if (tts.isSpeaking) tts.stop()
        tts.speak(sentence.contentEn, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    private fun deleteSentence(sentence: Sentence) {
        MaterialDialog(requireContext()).show {
            title(text = getString(R.string.delete_confirm_message, sentence.contentJp))
            positiveButton(text = "削除") { dictViewModel.deleteSentence(sentence) }
            negativeButton(text = "戻る")
        }
    }
}
