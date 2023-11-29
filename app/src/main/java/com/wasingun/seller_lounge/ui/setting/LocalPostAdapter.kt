package com.wasingun.seller_lounge.ui.setting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wasingun.seller_lounge.data.model.post.PostInfo
import com.wasingun.seller_lounge.databinding.ItemLocalPostBinding
import com.wasingun.seller_lounge.ui.home.PostClickListener

class LocalPostAdapter(private val postClickListener: PostClickListener) :
    ListAdapter<PostInfo, LocalPostAdapter.LocalPostViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocalPostViewHolder {
        return LocalPostViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: LocalPostViewHolder, position: Int) {
        holder.bind(getItem(position), postClickListener)
    }

    class LocalPostViewHolder(private val binding: ItemLocalPostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(postInfo: PostInfo, postClickListener: PostClickListener) {
            binding.postInfo = postInfo
            binding.postClickListener = postClickListener
        }

        companion object {

            fun from(parent: ViewGroup): LocalPostViewHolder {
                val binding =ItemLocalPostBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return LocalPostViewHolder(binding)
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