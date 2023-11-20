package com.wasingun.seller_lounge.ui.trendcomparison

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.wasingun.seller_lounge.R
import com.wasingun.seller_lounge.data.enums.ProductCategory
import com.wasingun.seller_lounge.databinding.FragmentTrendComparisonBinding
import com.wasingun.seller_lounge.extensions.showTextMessage
import com.wasingun.seller_lounge.ui.BaseFragment
import com.wasingun.seller_lounge.util.EventObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrendComparisonFragment : BaseFragment<FragmentTrendComparisonBinding>() {

    private val viewModel by viewModels<TrendComparisonViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setDropdownMenu()
        setIncorrectMessage()
        moveToResultScreen()
        setAPIErrorMessage()
    }

    private fun setDropdownMenu() {
        val categoryList = ProductCategory.values().map { it.categoryName }.take(10)
        val categoryArrayAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_sort, categoryList)
        binding.actvCategoryList.setAdapter(categoryArrayAdapter)

        binding.actvCategoryList.setDropDownBackgroundDrawable(
            ColorDrawable(ContextCompat.getColor(requireContext(), R.color.white))
        )
    }

    private fun setIncorrectMessage() {
        viewModel.snackbarText.observe(viewLifecycleOwner, EventObserver { resourceId ->
            binding.btnSearch.showTextMessage(resourceId)
        })
    }

    private fun moveToResultScreen() {
        viewModel.keywordResponseList.observe(viewLifecycleOwner, EventObserver {
            val action =
                TrendComparisonFragmentDirections.actionDestTrendComparisonToTrendComparisonResultFragment(
                    it
                )
            findNavController().navigate(action)
        })
    }

    private fun setAPIErrorMessage() {
        viewModel.isError.observe(viewLifecycleOwner, EventObserver {
            binding.btnSearch.showTextMessage(it)
        })
    }

    override fun getFragmentView(): Int {
        return R.layout.fragment_trend_comparison
    }
}