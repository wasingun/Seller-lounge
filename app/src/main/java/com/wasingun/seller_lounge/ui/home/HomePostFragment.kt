package com.wasingun.seller_lounge.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.wasingun.seller_lounge.R
import com.wasingun.seller_lounge.data.model.ProductCategory
import com.wasingun.seller_lounge.databinding.LayoutHomePostBinding
import com.wasingun.seller_lounge.extensions.showTextMessage
import com.wasingun.seller_lounge.network.NetworkConnection
import com.wasingun.seller_lounge.ui.BaseFragment
import com.wasingun.seller_lounge.constants.Constants
import com.wasingun.seller_lounge.util.getSerializableCompat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
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
        binding.rvPostList.setHasFixedSize(true)
        getPostList(category)
        observeError()
        showLoadingState()
        autoSearchTitleKeyword(category)
    }

    override fun onStart() {
        super.onStart()
        viewLifecycleOwner.lifecycleScope.launch {
            sharedViewModel.resetSearchKeyword()
        }
    }

    private fun getPostList(category: ProductCategory?) {
        val networkConnect = NetworkConnection(requireContext())
        networkConnect.observe(viewLifecycleOwner) { isConnected ->
            if (isConnected) {
                sharedViewModel.resetNetworkErrorMessage()
                viewModel.getRemotePostList()
                submitRemotePostList(category)
            } else {
                sharedViewModel.setNetworkErrorMessage(R.string.offline_mode)
                viewModel.getRecentViewedPostList()
                observeRecentViewedPost(category)
            }
        }
    }

    private fun observeError() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isError.collect { errorMessage ->
                    if (errorMessage != 0) {
                        binding.root.showTextMessage(errorMessage)
                    }
                }
            }
        }
    }

    private fun observeRecentViewedPost(category: ProductCategory?) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.localPostList.collect { postList ->
                    if (category == ProductCategory.ALL) {
                        adapter.submitList(postList)
                    } else {
                        val filteringPostList = postList.filter {
                            it.category == category
                        }
                        adapter.submitList(filteringPostList)
                    }
                }
            }
        }
    }

    private fun showLoadingState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoading.collect { state ->
                    if (state) {
                        binding.viewLoadingIndicator.visibility = View.VISIBLE
                    } else {
                        binding.viewLoadingIndicator.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun autoSearchTitleKeyword(category: ProductCategory?) {
        val networkConnection = NetworkConnection(requireContext())
        networkConnection.observe(viewLifecycleOwner) { isConnected ->
            if (isConnected) {
                searchRemotePost(category)
            } else {
                searchRecentViewedPost(category)
            }
        }
    }

    private fun searchRemotePost(category: ProductCategory?) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.searchKeyword.debounce(500).distinctUntilChanged().collect { keyword ->
                    val currentList = if (category == ProductCategory.ALL) {
                        viewModel.remotePostList.value
                    } else {
                        viewModel.remotePostList.value.filter { it.category == category }
                    }
                    if (keyword.isNotBlank()) {
                        adapter.submitList(currentList.filter { it.title.contains(keyword) })
                    } else  {
                        adapter.submitList(currentList)
                    }
                }
            }
        }
    }

    private fun searchRecentViewedPost(category: ProductCategory?) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.searchKeyword.debounce(500).distinctUntilChanged().collect { keyword ->
                    val recentViewedList = if (category == ProductCategory.ALL) {
                        viewModel.localPostList.value
                    } else {
                        viewModel.localPostList.value.filter { it.category == category }
                    }
                    if (keyword.isNotBlank()) {
                        adapter.submitList(recentViewedList.filter { it.title.contains(keyword) })
                    } else {
                        adapter.submitList(recentViewedList)
                    }
                }
            }
        }
    }

    private fun submitRemotePostList(category: ProductCategory?) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.remotePostList.flowWithLifecycle(
                    viewLifecycleOwner.lifecycle,
                    Lifecycle.State.STARTED
                ).collect { postList ->
                    if (category == ProductCategory.ALL) {
                        adapter.submitList(postList)
                    } else {
                        val filteringPostList = postList.filter {
                            it.category == category
                        }
                        adapter.submitList(filteringPostList)
                    }
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