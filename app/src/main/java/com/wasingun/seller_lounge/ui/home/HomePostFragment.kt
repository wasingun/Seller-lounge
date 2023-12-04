package com.wasingun.seller_lounge.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.wasingun.seller_lounge.R
import com.wasingun.seller_lounge.data.model.ProductCategory
import com.wasingun.seller_lounge.databinding.LayoutHomePostBinding
import com.wasingun.seller_lounge.extensions.showTextMessage
import com.wasingun.seller_lounge.network.NetworkConnection
import com.wasingun.seller_lounge.ui.BaseFragment
import com.wasingun.seller_lounge.util.Constants
import com.wasingun.seller_lounge.util.getSerializableCompat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomePostFragment : BaseFragment<LayoutHomePostBinding>() {
    private val adapter = HomePostAdapter { postInfo ->
        val action = HomeFragmentDirections.actionDestHomeToPostDetailFragment(postInfo)
        findNavController().navigate(action)
    }

    private val viewModel by viewModels<HomePostViewModel>()
    private val sharedViewModel by viewModels<HomeSharedViewModel>(
        ownerProducer = { requireParentFragment() }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val category =
            arguments?.getSerializableCompat(Constants.KEY_CATEGORY, ProductCategory::class.java)
        binding.rvPostList.adapter = adapter
        getPostList(category)
        observeRecentViewedPost(category)
        observeError()
        searchTitleKeyword(category)
        showLoadingState()
    }

    private fun getPostList(category: ProductCategory?) {
        val networkConnect = NetworkConnection(requireContext())
        networkConnect.observe(viewLifecycleOwner) { isConnected ->
            if (isConnected) {
                viewModel.getRemotePostList()
                submitRemotePostList(category)
            } else {
                binding.root.showTextMessage(R.string.offline_mode)
                viewModel.getRecentViewedPostList()
            }
        }
    }

    private fun observeError() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isError.collect { errorMessage ->
                if (errorMessage != 0) {
                    binding.root.showTextMessage(errorMessage)
                }
            }
        }
    }

    private fun observeRecentViewedPost(category: ProductCategory?) {
        lifecycleScope.launch {
            viewModel.localPostList.collect { postList ->
                if (category == ProductCategory.ALL) {
                    adapter.submitPost(postList)
                } else {
                    val filteringPostList = postList.filter {
                        it.category == category
                    }
                    adapter.submitPost(filteringPostList)
                }
            }
        }
    }

    private fun showLoadingState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { state ->
                if (state) {
                    binding.viewLoadingIndicator.visibility = View.VISIBLE
                } else {
                    binding.viewLoadingIndicator.visibility = View.GONE
                }
            }
        }
    }

    private fun searchTitleKeyword(category: ProductCategory?) {
        lifecycleScope.launch {
            sharedViewModel.searchButtonState.collect { state ->
                if (state) {
                    val currentList = if (category == ProductCategory.ALL) {
                        viewModel.remotePostList.value
                    } else {
                        viewModel.remotePostList.value.filter { it.category == category }
                    }
                    val currentKeyword = sharedViewModel.searchKeyword.value

                    if (sharedViewModel.searchKeyword.value.isNotBlank()) {
                        adapter.submitPost(currentList.filter { it.title.contains(currentKeyword) })
                    } else {
                        adapter.submitPost(currentList)
                    }
                }
            }
        }
    }

    private fun submitRemotePostList(category: ProductCategory?) {
        lifecycleScope.launch {
            viewModel.remotePostList.flowWithLifecycle(
                viewLifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            ).collect { postList ->
                if (category == ProductCategory.ALL) {
                    adapter.submitPost(postList)
                } else {
                    val filteringPostList = postList.filter {
                        it.category == category
                    }
                    adapter.submitPost(filteringPostList)
                }
            }
        }
    }

    companion object {
        fun newInstance(position: Int): HomePostFragment {
            val fragment = HomePostFragment()
            val bundle = Bundle().apply {
                putSerializable(Constants.KEY_CATEGORY, ProductCategory.values()[position])
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getFragmentView(): Int {
        return R.layout.layout_home_post
    }
}