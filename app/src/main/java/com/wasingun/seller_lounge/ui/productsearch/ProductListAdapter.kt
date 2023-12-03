package com.wasingun.seller_lounge.ui.productsearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wasingun.seller_lounge.data.model.productsearch.ProductInfo
import com.wasingun.seller_lounge.databinding.ItemProductContentBinding

class ProductListAdapter(private val clickListener: ProductClickListener) :
    PagingDataAdapter<ProductInfo, ProductListAdapter.ProductInfoViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductInfoViewHolder {
        return ProductInfoViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ProductInfoViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    class ProductInfoViewHolder(private val binding: ItemProductContentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(productInfo: ProductInfo?, clickListener: ProductClickListener) {
            binding.productInfo = productInfo
            binding.productClickListener = clickListener
        }

        companion object {

            fun from(parent: ViewGroup): ProductInfoViewHolder {
                val binding = ItemProductContentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ProductInfoViewHolder(binding)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ProductInfo>() {
        override fun areItemsTheSame(oldItem: ProductInfo, newItem: ProductInfo): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(oldItem: ProductInfo, newItem: ProductInfo): Boolean {
            return oldItem == newItem
        }
    }
}