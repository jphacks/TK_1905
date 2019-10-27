package jp.co.myowndict.view.quiz

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.wada811.databinding.dataBinding
import dagger.android.support.DaggerFragment
import jp.co.myowndict.R
import jp.co.myowndict.databinding.FragmentQuizBinding
import jp.co.myowndict.databinding.ItemQuizBinding
import jp.co.myowndict.extensions.observeNonNull
import jp.co.myowndict.model.Sentence
import jp.co.myowndict.view.common.DataBoundListAdapter
import jp.co.myowndict.view.common.SimpleDiffUtil
import jp.co.myowndict.view.updateAppBar
import java.util.*
import javax.inject.Inject

class QuizFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: QuizViewModel by viewModels { viewModelFactory }
    private val binding by dataBinding<FragmentQuizBinding>(R.layout.fragment_quiz)
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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        updateAppBar("Practice")
        val adapter = QuizPagerAdapter(viewLifecycleOwner,
            onClick = { sentence -> speakSentence(sentence) }
        )
        binding.quizViewPager.adapter = adapter
        viewModel.sentences.observeNonNull(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        viewModel.getSentences()
    }

    override fun onDestroy() {
        tts.shutdown()
        super.onDestroy()
    }

    private fun speakSentence(sentence: Sentence) {
        if (tts.isSpeaking) tts.stop()
        tts.speak(sentence.contentEn, TextToSpeech.QUEUE_FLUSH, null, "")
    }
}

class QuizPagerAdapter(
    lifecycleOwner: LifecycleOwner,
    private val onClick: ((Sentence) -> Unit)
) : DataBoundListAdapter<Sentence, ItemQuizBinding>(
    lifecycleOwner, SimpleDiffUtil()
) {
    override fun createBinding(parent: ViewGroup): ItemQuizBinding =
        ItemQuizBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    override fun bind(binding: ItemQuizBinding, item: Sentence) {
        binding.sentence = item
        binding.root.setOnClickListener {
            if (binding.englishTextView.isVisible) onClick(item)
        }
        binding.displayButton.setOnClickListener {
            binding.englishTextView.isVisible = true
            binding.displayButton.text = "右にスワイプ>"
        }
    }
}
