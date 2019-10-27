package jp.co.myowndict.view.common

import androidx.recyclerview.widget.DiffUtil

open class SimpleDiffUtil<T> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem

    /**
     * This method is called only if areItemsTheSame(T, T) returns true for
     * these items. It is obvious that the content is the same.
     */
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = true
}