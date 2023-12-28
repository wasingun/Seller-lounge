package com.wasingun.seller_lounge.ui.trendcomparison

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.wasingun.seller_lounge.R
import com.wasingun.seller_lounge.data.model.ProductCategory
import com.wasingun.seller_lounge.databinding.FragmentTrendComparisonBinding
import com.wasingun.seller_lounge.extensions.setClickEvent
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
        setInputErrorMessage()
        moveToResultScreen()
        setAPIErrorMessage()
        requestResult()
    }

    override fun onStart() {
        super.onStart()
        setDropdownMenu()
    }

    private fun setDropdownMenu() {
        val categoryList = ProductCategory.values().map { it.categoryName }.slice(1..10)
        val categoryArrayAdapter =
            ArrayAdapter(requireContext(), R.layout.layout_dropdown, categoryList)
        binding.actvCategoryList.setAdapter(categoryArrayAdapter)

        binding.actvCategoryList.setDropDownBackgroundDrawable(
            ColorDrawable(ContextCompat.getColor(requireContext(), R.color.white))
        )
    }

    private fun setInputErrorMessage() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isInputError.collect { resourceId ->
                    if (resourceId in inputErrorMessageList) {
                        binding.btnSearch.showTextMessage(resourceId)
                        viewModel.resetInputErrorMessage()
                    }
                }
            }
        }
    }

    private fun moveToResultScreen() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.keywordResponseResult.collect {
                    if (it?.endDate != null) {
                        val action =
                            TrendComparisonFragmentDirections.actionDestTrendComparisonToDestTrendComparisonResult(
                                it
                            )
                        viewModel.resetKeywordResponse()
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    private fun requestResult() {
        binding.btnSearch.setClickEvent(lifecycleScope) {
            viewModel.requestResult()
        }
    }

    private fun setAPIErrorMessage() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isNetworkError.collect { resourceId ->
                    if (resourceId in apiErrorMessage) {
                        binding.btnSearch.showTextMessage(resourceId)
                        viewModel.resetNetworkErrorMessage()
                    }
                }
            }
        }
    }

    override fun getFragmentView(): Int {
        return R.layout.fragment_trend_comparison
    }
}