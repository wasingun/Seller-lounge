package com.wasingun.seller_lounge.ui.setting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.wasingun.seller_lounge.R
import com.wasingun.seller_lounge.data.model.post.PostInfo
import com.wasingun.seller_lounge.databinding.FragmentRecentlyViewedPostBinding
import com.wasingun.seller_lounge.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecentlyViewedPostFragment : BaseFragment<FragmentRecentlyViewedPostBinding>() {
    private val adapter = LocalPostAdapter { postInfo ->
        moveToPostDetail(postInfo)
    }

    private fun moveToPostDetail(postInfo: PostInfo) {
        val action =
            RecentlyViewedPostFragmentDirections.actionDestRecentlyViewedPostToDestPostDetail(
                postInfo
            )
        findNavController().navigate(action)
    }

    private val viewModel: RecentlyViewedPostViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvLocalPostList.adapter = adapter
        lifecycleScope.launch {
            val localPostList = viewModel.getLocalPostList()
            adapter.submitList(localPostList)
        }
    }

    override fun getFragmentView(): Int {
        return R.layout.fragment_recently_viewed_post
    }
}