package com.wasingun.seller_lounge.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wasingun.seller_lounge.data.model.post.PostInfo
import com.wasingun.seller_lounge.databinding.ItemHomePostBinding

class HomePostAdapter(private val postClickListener: PostClickListener) :
    RecyclerView.Adapter<HomePostAdapter.HomePostViewHolder>() {

    private var postList: List<PostInfo> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePostViewHolder{
        return HomePostViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: HomePostViewHolder, position: Int) {
        holder.bind(postList[position], postClickListener)
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
}