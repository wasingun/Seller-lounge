package com.wasingun.seller_lounge.ui.postdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wasingun.seller_lounge.databinding.ItemPostImageBinding
import com.wasingun.seller_lounge.extensions.setImageList

class PostImageAdapter() :
    ListAdapter<String, PostImageAdapter.ImageBannerViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageBannerViewHolder {
        return ImageBannerViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ImageBannerViewHolder, position: Int) {
        return holder.bind(getItem(position))
    }

    class ImageBannerViewHolder(private val binding: ItemPostImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(imageUrl: String) {
            binding.ivPostImage.setImageList(imageUrl)
        }

        companion object {

            fun from(parent: ViewGroup): PostImageAdapter.ImageBannerViewHolder {
                val binding = ItemPostImageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ImageBannerViewHolder(binding)
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<String?>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}