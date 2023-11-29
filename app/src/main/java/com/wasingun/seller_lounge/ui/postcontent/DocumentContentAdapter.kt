package com.wasingun.seller_lounge.ui.postcontent

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wasingun.seller_lounge.data.model.attachedcontent.DocumentContent
import com.wasingun.seller_lounge.databinding.ItemDocumentContentBinding

class DocumentContentAdapter(private val documentDeleteListener: DocumentDeleteListener) :
    ListAdapter<DocumentContent, DocumentContentAdapter.DocumentContentViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentContentViewHolder {
        return DocumentContentViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: DocumentContentViewHolder, position: Int) {
        holder.bind(getItem(position), documentDeleteListener)
    }

    class DocumentContentViewHolder(private val binding: ItemDocumentContentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(documentContent: DocumentContent, documentDeleteListener: DocumentDeleteListener) {
            binding.fileName = documentContent.fileName
            binding.listener = documentDeleteListener
            binding.documentContent = documentContent
        }

        companion object {

            fun from(parent: ViewGroup): DocumentContentViewHolder {
                val binding = ItemDocumentContentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return DocumentContentViewHolder(binding)
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<DocumentContent>() {
        override fun areItemsTheSame(oldItem: DocumentContent, newItem: DocumentContent): Boolean {
            return oldItem.uri == newItem.uri
        }

        override fun areContentsTheSame(
            oldItem: DocumentContent,
            newItem: DocumentContent
        ): Boolean {
            return oldItem == newItem
        }
    }
}