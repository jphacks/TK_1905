package jp.co.myowndict.view.main

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.wada811.databinding.dataBinding
import dagger.android.support.DaggerFragment
import jp.co.myowndict.R
import jp.co.myowndict.databinding.FragmentDictBinding
import jp.co.myowndict.extensions.observeNonNull
import jp.co.myowndict.view.MainViewModel
import javax.inject.Inject

class DictFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val mainViewModel: MainViewModel by activityViewModels { viewModelFactory }
    private val dictViewModel: DictViewModel by viewModels { viewModelFactory }
    private val binding by dataBinding<FragmentDictBinding>(R.layout.fragment_dict)
    private lateinit var adapter: SentenceAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.viewModel = mainViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ViewCompat.setOnApplyWindowInsetsListener(binding.rootView) { v, insets ->
            v.updatePadding(
                bottom = insets.systemWindowInsetBottom,
                top = insets.systemWindowInsetTop
            )
            insets
        }

        adapter = SentenceAdapter(viewLifecycleOwner,
            onClick = {},
            onLongClick = { sentence ->
                MaterialDialog(requireContext()).show {
                    title(text = getString(R.string.delete_confirm_message, sentence.contentJp))
                    positiveButton(text = "OK") { dictViewModel.deleteSentence(sentence) }
                    negativeButton(text = "NG")
                }
            }
        )
        binding.recyclerView.adapter = adapter
        binding.refreshLayout.setOnRefreshListener {
            dictViewModel.getSentences()
        }
        dictViewModel.getSentences()

        observe()
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
    }

    fun hideTag() {
        if (!isAdded) return
        binding.slideBar.x = binding.root.width.toFloat()
    }
}
