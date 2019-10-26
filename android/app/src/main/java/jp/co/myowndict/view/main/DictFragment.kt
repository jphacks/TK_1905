package jp.co.myowndict.view.main

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
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
        adapter = SentenceAdapter(viewLifecycleOwner) {
            // OnClick
        }
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
