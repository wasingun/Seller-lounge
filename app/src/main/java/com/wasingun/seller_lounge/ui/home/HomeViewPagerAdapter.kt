package com.wasingun.seller_lounge.ui.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wasingun.seller_lounge.data.enums.ProductCategory

class HomeViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val categories = ProductCategory.values()

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun createFragment(position: Int): Fragment {
        return HomePostFragment.newInstance(position)
    }
}