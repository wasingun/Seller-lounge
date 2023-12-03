package com.wasingun.seller_lounge.ui.trendcomparison

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wasingun.seller_lounge.R
import com.wasingun.seller_lounge.data.model.ProductCategory
import com.wasingun.seller_lounge.data.model.trendcomparison.KeywordDetail
import com.wasingun.seller_lounge.data.model.trendcomparison.KeywordRequest
import com.wasingun.seller_lounge.data.model.trendcomparison.KeywordResponse
import com.wasingun.seller_lounge.data.repository.TrendComparisonRepository
import com.wasingun.seller_lounge.util.Constants
import com.wasingun.seller_lounge.util.latestDateFormat
import com.wasingun.seller_lounge.util.thirtyDaysAgoDateFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrendComparisonViewModel @Inject constructor(private val repository: TrendComparisonRepository) :
    ViewModel() {

    private val _snackbarText = MutableStateFlow(0)
    val snackbarText: StateFlow<Int> = _snackbarText
    private val _isError = MutableStateFlow<Int>(0)
    val isError: StateFlow<Int> = _isError
    private val _keywordResponseResult = MutableStateFlow<KeywordResponse?>(null)
    val keywordResponseResult: StateFlow<KeywordResponse?> = _keywordResponseResult

    val keyword = MutableStateFlow<String?>(null)
    val category = MutableStateFlow<String?>(null)

    fun requestResult() {
        val currentKeyword = keyword.value ?: ""
        val currentCategory = category.value ?: ""
        val categoryCode =
            ProductCategory.values().firstOrNull { it.categoryName == currentCategory }?.id ?: ""
        val currentKeywordList = currentKeyword
            .split(",")
            .map { KeywordDetail(it, listOf(it)) }
        val currentKeywordCount = currentKeywordList.size

        if (isValidInfo(currentKeyword, currentCategory, currentKeywordCount)) {
            return
        }

        val oneDaysAgo = latestDateFormat(System.currentTimeMillis())
        val thirtyDaysAgo = thirtyDaysAgoDateFormat(System.currentTimeMillis())
        val timeUnit = Constants.DATE
        viewModelScope.launch {
            repository.requestComparisonResult(
                KeywordRequest(
                    thirtyDaysAgo,
                    oneDaysAgo,
                    timeUnit,
                    categoryCode,
                    currentKeywordList
                ),
                onComplete = {
                    _keywordResponseResult.value = it
                },
                onError = { _isError.value = it }
            ).collect()
        }
    }

    private fun isValidInfo(keyword: String, category: String, keywordCount: Int): Boolean {
        if (keyword.isBlank()) {
            _snackbarText.value = R.string.announce_blank_keyword
            return true
        } else if (category.isBlank()) {
            _snackbarText.value = R.string.announce_blank_category
            return true
        } else if (keywordCount > 5) {
            _snackbarText.value = R.string.keyword_count_over
            return true
        }
        return false
    }

    fun resetKeywordResponse() {
        _keywordResponseResult.value = null
    }
}