package com.erdiansyah.mystory.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.erdiansyah.mystory.R
import com.erdiansyah.mystory.data.remote.ListStoryItem
import com.erdiansyah.mystory.databinding.StoryItemBinding
import com.erdiansyah.mystory.util.DateFormatter
import com.erdiansyah.mystory.util.DiffUtilList
import java.util.*

class ListAdapter : PagingDataAdapter<ListStoryItem, ListAdapter.ListViewHolder>(DIFF_CALLBACK){
    private var onItemClickListener: OnItemClickListener? = null
    interface OnItemClickListener{
        fun onItemClicked(data: ListStoryItem)
    }
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        this.onItemClickListener = onItemClickListener
    }

    inner class ListViewHolder(var binding: StoryItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.apply {
                binding.apply {
                    tvUsername.text = data.name
                    Glide.with(holder.itemView.context)
                        .load(data.photoUrl)
                        .centerCrop()
                        .error(R.drawable.ic_baseline_broken_image_24)
                        .into(storyImage)
                    datePublish.text = DateFormatter.formatDate(data.createdAt, TimeZone.getDefault().id)
                }
                itemView.setOnClickListener{
                    onItemClickListener?.onItemClicked(data)
                }
            }
        }
    }

//    fun setListItem(item: List<ListStoryItem>){
//        val diffUtil = DiffUtilList(listItem, item)
//        val diffResults = DiffUtil.calculateDiff(diffUtil)
//        this.listItem = item
//        diffResults.dispatchUpdatesTo(this)
//    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}