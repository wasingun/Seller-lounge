package com.wasingun.seller_lounge.ui.home

import android.os.Bundle
import android.view.View
import com.wasingun.seller_lounge.R
import com.wasingun.seller_lounge.data.enums.ProductCategory
import com.wasingun.seller_lounge.databinding.LayoutHomePostBinding
import com.wasingun.seller_lounge.ui.BaseFragment
import com.wasingun.seller_lounge.util.Constants

class HomePostFragment: BaseFragment<LayoutHomePostBinding>() {

    private lateinit var adapter: HomePostAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = HomePostAdapter(PostClickListener {

        })
        binding.rvPostList.adapter = adapter
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