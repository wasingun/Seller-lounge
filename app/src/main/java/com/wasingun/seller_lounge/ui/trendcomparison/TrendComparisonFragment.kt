package com.wasingun.seller_lounge.ui.trendcomparison

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.wasingun.seller_lounge.R
import com.wasingun.seller_lounge.SellerLoungeApplication
import com.wasingun.seller_lounge.data.repository.TrendComparisonRepository
import com.wasingun.seller_lounge.databinding.FragmentTrendComparisonBinding
import com.wasingun.seller_lounge.extensions.showTextMessage
import com.wasingun.seller_lounge.ui.BaseFragment
import com.wasingun.seller_lounge.util.EventObserver

class TrendComparisonFragment : BaseFragment<FragmentTrendComparisonBinding>() {

    private val viewModel by viewModels<TrendComparisonViewModel>{
        TrendComparisonViewModel.provideFactory(TrendComparisonRepository(SellerLoungeApplication.appContainer.provideApiClient()))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.executePendingBindings()
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        val categoryList = resources.getStringArray(R.array.category_select)
        val categoryArrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_sort, categoryList)
        binding.tvCategoryList.setAdapter(categoryArrayAdapter)

        binding.tvCategoryList.setDropDownBackgroundDrawable(
            ColorDrawable(ContextCompat.getColor(requireContext(),R.color.white))
        )
        setFailMessage()
    }

    private fun setFailMessage() {
        viewModel.snackbarText.observe(viewLifecycleOwner,EventObserver {resourceId ->
            binding.btnSearch.showTextMessage(resourceId)
        })
    }

    private fun setMoveToScreen() {
        viewModel.moveToScreen.observe(viewLifecycleOwner, EventObserver {moveToScreen ->
        })
    }

    override fun getFragmentView(): Int {
        return R.layout.fragment_trend_comparison
    }
}