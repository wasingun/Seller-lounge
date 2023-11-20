package com.wasingun.seller_lounge.ui.home

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.wasingun.seller_lounge.R
import com.wasingun.seller_lounge.SellerLoungeApplication
import com.wasingun.seller_lounge.data.enums.ProductCategory
import com.wasingun.seller_lounge.databinding.FragmentHomeBinding
import com.wasingun.seller_lounge.ui.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private lateinit var adapter: HomeViewPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnPost.setOnClickListener {
            moveToPost()
        }
        adapter = HomeViewPagerAdapter(this)
        binding.vpHome.adapter = adapter

        TabLayoutMediator(binding.tlTabMenu, binding.vpHome) { tab, position ->
            tab.text = ProductCategory.values()[position].categoryName
        }.attach()
    }

    private fun moveToPost() {
        val action = HomeFragmentDirections.actionDestHomeToPostContentFragment()
        findNavController().navigate(action)
    }

    override fun onStart() {
        super.onStart()
        if (SellerLoungeApplication.auth.currentUser == null) {
            val action = HomeFragmentDirections.actionDestHomeToDestLogin()
            findNavController().navigate(action)
        }
    }

    override fun getFragmentView(): Int {
        return R.layout.fragment_home
    }
}