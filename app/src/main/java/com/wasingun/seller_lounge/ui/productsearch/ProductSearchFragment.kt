package com.wasingun.seller_lounge.ui.productsearch

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.wasingun.seller_lounge.R
import com.wasingun.seller_lounge.data.enums.SortSearchType
import com.wasingun.seller_lounge.databinding.FragmentProductSearchBinding
import com.wasingun.seller_lounge.ui.BaseFragment

class ProductSearchFragment : BaseFragment<FragmentProductSearchBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sortTypeList = SortSearchType.values().map{
            it.sortType
        }
        val sortTypeArrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_sort, sortTypeList)
        binding.actvSelectedSort.setAdapter(sortTypeArrayAdapter)

        binding.actvSelectedSort.setDropDownBackgroundDrawable(
            ColorDrawable(ContextCompat.getColor(requireContext(),R.color.white))
        )
    }

    override fun getFragmentView(): Int {
        return R.layout.fragment_product_search
    }
}