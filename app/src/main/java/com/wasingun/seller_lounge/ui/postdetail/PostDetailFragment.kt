package com.wasingun.seller_lounge.ui.postdetail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.wasingun.seller_lounge.R
import com.wasingun.seller_lounge.data.model.post.PostInfo
import com.wasingun.seller_lounge.databinding.FragmentPostDetailBinding
import com.wasingun.seller_lounge.extensions.setCircleImage
import com.wasingun.seller_lounge.extensions.showTextMessage
import com.wasingun.seller_lounge.ui.BaseFragment
import com.wasingun.seller_lounge.util.convertDisplayedDate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostDetailFragment : BaseFragment<FragmentPostDetailBinding>() {
    private val args: PostDetailFragmentArgs by navArgs()
    private val adapter = PostImageAdapter()
    private val viewModel: PostDetailViewModel by viewModels<PostDetailViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val postInfo = args.post
        getWriterInfo()
        setLayout(postInfo)
        setWriterInfo()
        lifecycleScope.launch {
            viewModel.isError.collect{errorMessage ->
                if(errorMessage != 0) {
                    binding.root.showTextMessage(errorMessage)
                }
            }
        }
    }

    private fun getWriterInfo() {
        lifecycleScope.launch {
            viewModel.getWriterInfo(args.post.userId)
        }
    }

    private fun setWriterInfo() {
        lifecycleScope.launch {
            viewModel.writerInfo.collect { writerInfo ->
                binding.tvUserName.text = writerInfo.userName
                binding.ivProfileImage.setCircleImage(writerInfo.userImage)
            }
        }
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
        if (postInfo.imageList.isNullOrEmpty()) {
            binding.vpImageList.visibility = View.GONE
        } else {
            adapter.submitList(postInfo.imageList)
        }

        TabLayoutMediator(binding.tlCircleIndicator, binding.vpImageList) { tab, position ->

        }.attach()
    }

    override fun getFragmentView(): Int {
        return R.layout.fragment_post_detail
    }
}