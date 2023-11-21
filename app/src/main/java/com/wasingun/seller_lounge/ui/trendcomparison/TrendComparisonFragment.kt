package com.wasingun.seller_lounge.ui.trendcomparison

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.wasingun.seller_lounge.R
import com.wasingun.seller_lounge.data.enums.ProductCategory
import com.wasingun.seller_lounge.databinding.FragmentTrendComparisonBinding
import com.wasingun.seller_lounge.extensions.showTextMessage
import com.wasingun.seller_lounge.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TrendComparisonFragment : BaseFragment<FragmentTrendComparisonBinding>() {

    private val viewModel by viewModels<TrendComparisonViewModel>()
    private val inputErrorMessageList = listOf(
        R.string.announce_blank_category,
        R.string.announce_blank_keyword,
        R.string.keyword_count_over
    )
    private val apiErrorMessage = listOf(
        R.string.error_api_network,
        R.string.error_api_http_response
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setDropdownMenu()
        setInputErrorMessage()
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

    private fun setInputErrorMessage() {
        lifecycleScope.launch {
            viewModel.snackbarText.collect { resourceId ->
                if (resourceId in inputErrorMessageList) {
                    binding.btnSearch.showTextMessage(resourceId)
                }
            }
        }
    }

    private fun moveToResultScreen() {
        lifecycleScope.launch {
            viewModel.keywordResponseResult.collect {
                if (it?.endDate != null) {
                    val action =
                        TrendComparisonFragmentDirections.actionDestTrendComparisonToTrendComparisonResultFragment(
                            it
                        )
                    viewModel.resetKeywordResponse()
                    findNavController().navigate(action)
                }
            }
        }
    }

    private fun setAPIErrorMessage() {
        lifecycleScope.launch {
            viewModel.isError.collect { resourceId ->
                if (resourceId in apiErrorMessage) {
                    binding.btnSearch.showTextMessage(resourceId)
                }
            }
        }
    }

    override fun getFragmentView(): Int {
        return R.layout.fragment_trend_comparison
    }
}