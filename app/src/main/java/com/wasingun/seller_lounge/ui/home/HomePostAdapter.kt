package com.wasingun.seller_lounge.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wasingun.seller_lounge.data.model.post.PostInfo
import com.wasingun.seller_lounge.databinding.ItemHomePostBinding

class HomePostAdapter(private val postClickListener: PostClickListener) :
    ListAdapter<PostInfo, HomePostAdapter.HomePostViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePostViewHolder {
        return HomePostViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: HomePostViewHolder, position: Int) {
        holder.bind(getItem(position), postClickListener)
    }

    class HomePostViewHolder(private val binding: ItemHomePostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(postInfo: PostInfo, postClickListener: PostClickListener) {
            binding.postInfo = postInfo
            binding.postClickListener = postClickListener
        }

        companion object {

            fun from(parent: ViewGroup): HomePostViewHolder {
                val binding = ItemHomePostBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return HomePostViewHolder(binding)
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<PostInfo>() {
        override fun areItemsTheSame(oldItem: PostInfo, newItem: PostInfo): Boolean {
            return oldItem.postId == newItem.postId
        }

        override fun areContentsTheSame(oldItem: PostInfo, newItem: PostInfo): Boolean {
            return oldItem == newItem
        }
    }
}