package com.wasingun.seller_lounge.ui.productsearch

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.wasingun.seller_lounge.R
import com.wasingun.seller_lounge.databinding.FragmentProductSearchBinding
import com.wasingun.seller_lounge.ui.BaseFragment

class ProductSearchFragment : BaseFragment<FragmentProductSearchBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sortType = resources.getStringArray(R.array.sort_select)
        val sortTypeArrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_sort, sortType)
        binding.tvSelectedSort.setAdapter(sortTypeArrayAdapter)

        binding.tvSelectedSort.setDropDownBackgroundDrawable(
            ColorDrawable(ContextCompat.getColor(requireContext(),R.color.white))
        )
    }

    override fun getFragmentView(): Int {
        return R.layout.fragment_product_search
    }
}