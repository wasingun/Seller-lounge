package com.wasingun.seller_lounge.ui.postdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wasingun.seller_lounge.databinding.ItemAttachedDocumentBinding

class PostDetailAttachedDocumentAdapter(private val attachedFileClickListener: AttachedFileClickListener) :
    ListAdapter<String, PostDetailAttachedDocumentAdapter.AttachedDocumentViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachedDocumentViewHolder {
        return AttachedDocumentViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: AttachedDocumentViewHolder, position: Int) {
        return holder.bind(getItem(position), attachedFileClickListener)
    }

    class AttachedDocumentViewHolder(private val binding: ItemAttachedDocumentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(fileName: String, attachedFileClickListener: AttachedFileClickListener) {
            binding.fileName = fileName
            binding.attachedFileClickListener = attachedFileClickListener
        }

        companion object {

            fun from(parent: ViewGroup): AttachedDocumentViewHolder {
                val binding = ItemAttachedDocumentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return AttachedDocumentViewHolder(binding)
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