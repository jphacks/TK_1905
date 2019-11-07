package jp.co.myowndict.view.main

import android.view.LayoutInflater
import android.view.Menu
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import jp.co.myowndict.R
import jp.co.myowndict.databinding.ItemSentenceBinding
import jp.co.myowndict.model.Sentence
import jp.co.myowndict.view.common.DataBoundListAdapter
import jp.co.myowndict.view.common.SimpleDiffUtil

class SentenceAdapter(
    lifecycleOwner: LifecycleOwner,
    private val onClickItem: ((Sentence) -> Unit),
    private val onClickEditMenu: ((Sentence) -> Unit),
    private val onClickDeleteMenu: ((Sentence) -> Unit)
) : DataBoundListAdapter<Sentence, ItemSentenceBinding>(lifecycleOwner, SimpleDiffUtil()) {

    override fun createBinding(parent: ViewGroup): ItemSentenceBinding =
        ItemSentenceBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    override fun bind(binding: ItemSentenceBinding, item: Sentence) {
        binding.sentence = item
        binding.root.setOnClickListener {
            onClickItem(item)
        }
        binding.root.setOnCreateContextMenuListener { menu, _, _ ->
            menu.add(Menu.NONE, 1, 1, R.string.context_menu_edit)
                .setOnMenuItemClickListener {
                    onClickEditMenu(item)
                    true
                }
            menu.add(Menu.NONE, 2, 2, R.string.context_menu_delete)
                .setOnMenuItemClickListener {
                    onClickDeleteMenu(item)
                    true
                }
        }
    }
}