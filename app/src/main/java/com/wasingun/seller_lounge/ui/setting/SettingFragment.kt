package com.wasingun.seller_lounge.ui.setting

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.wasingun.seller_lounge.R
import com.wasingun.seller_lounge.SellerLoungeApplication
import com.wasingun.seller_lounge.databinding.FragmentSettingBinding
import com.wasingun.seller_lounge.ui.BaseFragment

class SettingFragment: BaseFragment<FragmentSettingBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.firebaseUser = SellerLoungeApplication.auth.currentUser
        binding.tvRecentlyViewedPost.setOnClickListener {
            val action = SettingFragmentDirections.actionDestSettingToRecentlyViewedPostFragment()
            findNavController().navigate(action)
        }
    }

    override fun getFragmentView(): Int {
        return R.layout.fragment_setting
    }
}