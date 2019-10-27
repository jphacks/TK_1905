package jp.co.myowndict.view.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import javax.inject.Inject

class QuizFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: QuizViewModel by viewModels { viewModelFactory }
    private val binding by dataBinding<FragmentQuizBinding>(R.layout.fragment_quiz)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        updateAppBar("Practice")
        val adapter = QuizPagerAdapter(viewLifecycleOwner)
        binding.quizViewPager.adapter = adapter
        viewModel.sentences.observeNonNull(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        viewModel.getSentences()
    }
}

class QuizPagerAdapter(
    lifecycleOwner: LifecycleOwner
) : DataBoundListAdapter<Sentence, ItemQuizBinding>(
    lifecycleOwner, SimpleDiffUtil()
) {
    override fun createBinding(parent: ViewGroup): ItemQuizBinding =
        ItemQuizBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    override fun bind(binding: ItemQuizBinding, item: Sentence) {
        binding.sentence = item
        binding.displayButton.setOnClickListener {
            binding.englishTextView.isVisible = true
            binding.displayButton.text = "右にスワイプ>"
        }
    }
}
