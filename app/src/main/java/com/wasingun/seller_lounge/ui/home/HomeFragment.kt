package com.wasingun.seller_lounge.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.wasingun.seller_lounge.R
import com.wasingun.seller_lounge.data.model.ProductCategory
import com.wasingun.seller_lounge.databinding.FragmentHomeBinding
import com.wasingun.seller_lounge.extensions.showTextMessage
import com.wasingun.seller_lounge.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private lateinit var adapter: HomeViewPagerAdapter
    private val sharedViewModel by viewModels<HomeSharedViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkLoginState()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = HomeViewPagerAdapter(this)
        checkLoginState()
        binding.vpHome.adapter = adapter
        binding.viewModel = sharedViewModel
        binding.btnPost.setOnClickListener {
            moveToPost()
        }
        setViewPager()
        setErrorMessage()
    }

    private fun setErrorMessage() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.isError.collect { errorMessage ->
                    if (errorMessage != 0) {
                        binding.root.showTextMessage(errorMessage)
                    }
                }
            }
        }
    }

    private fun setViewPager() {
        TabLayoutMediator(binding.tlTabMenu, binding.vpHome) { tab, position ->
            tab.text = ProductCategory.values()[position].categoryName
        }.attach()
    }

    private fun checkLoginState() {
        if (sharedViewModel.getCurrentUser() == null) {
            val action = HomeFragmentDirections.actionDestHomeToDestLogin()
            findNavController().navigate(action)
        } else {
            updateUserInfo()
        }
    }

    private fun moveToPost() {
        val action = HomeFragmentDirections.actionDestHomeToPostUploadFragment()
        findNavController().navigate(action)
    }

    private fun updateUserInfo() {
        sharedViewModel.updateUserInfo()
    }

    override fun getFragmentView(): Int {
        return R.layout.fragment_home
    }
}