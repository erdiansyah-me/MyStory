package com.erdiansyah.mystory.presenter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.erdiansyah.mystory.databinding.LoadingItemBinding

class LoadingStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<LoadingStateAdapter.LoadingStateViewHolder>() {
    inner class LoadingStateViewHolder(private val binding: LoadingItemBinding, retry: () -> Unit) :
        RecyclerView.ViewHolder(binding.root){
            init {
                binding.retryButton.setOnClickListener { retry.invoke() }
            }

            fun bind(loadState: LoadState) {
                if (loadState is LoadState.Error) {
                    binding.tvError.text = loadState.error.localizedMessage
                }
                binding.progressBar.isVisible = loadState is LoadState.Loading
                binding.retryButton.isVisible = loadState is LoadState.Error
                binding.tvError.isVisible = loadState is LoadState.Error
            }
    }

    override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadingStateViewHolder {
        val binding = LoadingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadingStateViewHolder(binding, retry)
    }
}