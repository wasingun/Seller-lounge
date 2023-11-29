package com.wasingun.seller_lounge.ui.setting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.wasingun.seller_lounge.R
import com.wasingun.seller_lounge.databinding.FragmentSettingBinding
import com.wasingun.seller_lounge.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment: BaseFragment<FragmentSettingBinding>() {
    private val viewModel: SettingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.firebaseUser = viewModel.getCurrentUser()
        binding.tvRecentlyViewedPost.setOnClickListener {
            val action = SettingFragmentDirections.actionDestSettingToRecentlyViewedPostFragment()
            findNavController().navigate(action)
        }
    }

    override fun getFragmentView(): Int {
        return R.layout.fragment_setting
    }
}