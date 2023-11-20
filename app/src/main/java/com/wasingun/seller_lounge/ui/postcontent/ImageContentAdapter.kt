package com.wasingun.seller_lounge.ui.postcontent

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wasingun.seller_lounge.data.model.ImageContent
import com.wasingun.seller_lounge.databinding.ItemImageContentBinding

class ImageContentAdapter(private val imageDeleteListener: ImageDeleteListener) :
    ListAdapter<ImageContent, ImageContentAdapter.ImageContentViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageContentViewHolder {
        return ImageContentViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ImageContentViewHolder, position: Int) {
        holder.bind(getItem(position), imageDeleteListener)
    }

    class ImageContentViewHolder(private val binding: ItemImageContentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(imageContent: ImageContent, deleteClickListener: ImageDeleteListener) {
            binding.imageContent = imageContent
            binding.listener = deleteClickListener
        }

        companion object {

            fun from(parent: ViewGroup): ImageContentViewHolder {
                val binding = ItemImageContentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ImageContentViewHolder(binding)
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<ImageContent>() {
        override fun areItemsTheSame(oldItem: ImageContent, newItem: ImageContent): Boolean {
            return oldItem.uri == newItem.uri
        }

        override fun areContentsTheSame(oldItem: ImageContent, newItem: ImageContent): Boolean {
            return oldItem == newItem
        }
    }
}