package com.wasingun.seller_lounge.ui.postdetail

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.wasingun.seller_lounge.R
import com.wasingun.seller_lounge.data.model.post.PostInfo
import com.wasingun.seller_lounge.databinding.FragmentPostDetailBinding
import com.wasingun.seller_lounge.ui.BaseFragment
import com.wasingun.seller_lounge.util.convertDisplayedDate

class PostDetailFragment : BaseFragment<FragmentPostDetailBinding>() {
    private val args: PostDetailFragmentArgs by navArgs()
    private val adapter = PostImageAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val postInfo = args.post

        setLayout(postInfo)
    }

    private fun setLayout(postInfo: PostInfo) {
        with(binding) {
            tvTitle.text = postInfo.title
            tvCategory.text = postInfo.category.categoryName
            tvBody.text = postInfo.body
            tvTime.convertDisplayedDate(postInfo.createTime)
            vpImageList.adapter = adapter
            btnArrow.setOnClickListener {
                findNavController().navigateUp()
            }
        }
        adapter.submitList(postInfo.imageList)

        TabLayoutMediator(binding.tlCircleIndicator, binding.vpImageList) { tab, position ->

        }.attach()
    }

    override fun getFragmentView(): Int {
        return R.layout.fragment_post_detail
    }
}