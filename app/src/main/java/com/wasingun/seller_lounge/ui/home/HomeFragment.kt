package com.wasingun.seller_lounge.ui.home

import androidx.navigation.fragment.findNavController
import com.wasingun.seller_lounge.R
import com.wasingun.seller_lounge.SellerLoungeApplication
import com.wasingun.seller_lounge.databinding.FragmentHomeBinding
import com.wasingun.seller_lounge.ui.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

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