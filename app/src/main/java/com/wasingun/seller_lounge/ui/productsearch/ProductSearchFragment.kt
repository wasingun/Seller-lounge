package com.wasingun.seller_lounge.ui.productsearch

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.wasingun.seller_lounge.R
import com.wasingun.seller_lounge.data.model.localpost.SortSearchType
import com.wasingun.seller_lounge.databinding.FragmentProductSearchBinding
import com.wasingun.seller_lounge.extensions.setClickEvent
import com.wasingun.seller_lounge.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductSearchFragment : BaseFragment<FragmentProductSearchBinding>() {
    private val viewModel: ProductSearchViewModel by viewModels()
    private val adapter = ProductListAdapter(ProductClickListener { productInfo ->
        val productLink = productInfo.link
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(productLink))
        startActivity(intent)
    })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvProductList.adapter = adapter
        binding.viewModel = viewModel
        val sortTypeList = SortSearchType.values().map {
            it.sortType
        }
        val sortTypeArrayAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_sort, sortTypeList)
        binding.actvSelectedSort.setAdapter(sortTypeArrayAdapter)

        binding.actvSelectedSort.setDropDownBackgroundDrawable(
            ColorDrawable(ContextCompat.getColor(requireContext(), R.color.white))
        )
        binding.btnSearchIcon.setClickEvent(lifecycleScope) {
            submitPagingData()
        }
    }

    private fun submitPagingData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getProductInfoList().collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }
    }

    override fun getFragmentView(): Int {
        return R.layout.fragment_product_search
    }
}