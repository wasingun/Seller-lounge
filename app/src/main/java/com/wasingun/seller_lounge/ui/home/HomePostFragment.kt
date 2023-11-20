package com.wasingun.seller_lounge.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.wasingun.seller_lounge.R
import com.wasingun.seller_lounge.data.enums.ProductCategory
import com.wasingun.seller_lounge.databinding.LayoutHomePostBinding
import com.wasingun.seller_lounge.ui.BaseFragment
import com.wasingun.seller_lounge.util.Constants
import com.wasingun.seller_lounge.util.getSerializableCompat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomePostFragment : BaseFragment<LayoutHomePostBinding>() {

    private val adapter = HomePostAdapter(PostClickListener {

    })
    private val viewModel by viewModels<HomePostViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val category =
            arguments?.getSerializableCompat(Constants.KEY_CATEGORY, ProductCategory::class.java)
        binding.rvPostList.adapter = adapter
        viewModel.getPost()
        getPostList(category)
    }

    private fun getPostList(category: ProductCategory?) {
        lifecycleScope.launch {
            viewModel.postList.flowWithLifecycle(
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