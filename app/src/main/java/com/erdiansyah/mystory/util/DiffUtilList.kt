package com.erdiansyah.mystory.util

import androidx.recyclerview.widget.DiffUtil
import com.erdiansyah.mystory.data.remote.ListStoryItem

class DiffUtilList(
    private val oldList: List<ListStoryItem>,
    private val newList: List<ListStoryItem>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList == newList
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList[oldItemPosition]
        val latest = newList[newItemPosition]
        return when {
            old.id == latest.id-> true
            old.photoUrl == latest.photoUrl -> true
            old.createdAt == latest.createdAt -> true
            old.name == latest.name -> true
            else -> false
        }
    }
}