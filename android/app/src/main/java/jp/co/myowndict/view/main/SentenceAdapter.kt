package jp.co.myowndict.view.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import jp.co.myowndict.databinding.ItemSentenceBinding
import jp.co.myowndict.model.Sentence
import jp.co.myowndict.view.common.DataBoundListAdapter
import jp.co.myowndict.view.common.SimpleDiffUtil

class SentenceAdapter(
    lifecycleOwner: LifecycleOwner,
    private val onClick: ((Sentence) -> Unit)?
) : DataBoundListAdapter<Sentence, ItemSentenceBinding>(lifecycleOwner, SimpleDiffUtil()) {

    override fun createBinding(parent: ViewGroup): ItemSentenceBinding =
        ItemSentenceBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    override fun bind(binding: ItemSentenceBinding, item: Sentence) {
        binding.sentence = item
        binding.root.setOnClickListener {
            onClick?.invoke(item)
        }
    }
}