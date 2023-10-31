package com.wasingun.seller_lounge.ui.trendcomparison

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.wasingun.seller_lounge.R
import com.wasingun.seller_lounge.data.enums.ProductCategory
import com.wasingun.seller_lounge.data.model.trendcomparison.KeywordDetail
import com.wasingun.seller_lounge.data.model.trendcomparison.KeywordRequest
import com.wasingun.seller_lounge.data.model.trendcomparison.KeywordResponse
import com.wasingun.seller_lounge.data.repository.TrendComparisonRepository
import com.wasingun.seller_lounge.util.Constants
import com.wasingun.seller_lounge.util.Event
import com.wasingun.seller_lounge.util.latestDateFormat
import com.wasingun.seller_lounge.util.thirtyDaysAgoDateFormat
import kotlinx.coroutines.launch

class TrendComparisonViewModel(private val repository: TrendComparisonRepository) : ViewModel() {

    private val _keywordResponseList = MutableLiveData<Event<KeywordResponse>>()
    val keywordResponseList: LiveData<Event<KeywordResponse>> = _keywordResponseList
    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText
    private val _isError = MutableLiveData<Event<Int>>()
    val isError: LiveData<Event<Int>> = _isError
    val keyword = MutableLiveData<String>()
    val category = MutableLiveData<String>()

    fun requestResult() {
        val currentKeyword = keyword.value ?: ""
        val currentCategory = category.value ?: ""
        val categoryCode =
            ProductCategory.values().firstOrNull { it.categoryName == currentCategory }?.id ?: ""
        val currentKeywordList = currentKeyword
            .split(",")
            .map{it -> KeywordDetail(it, listOf(it))}
        val currentKeywordCount = currentKeywordList.size

        if (isValidInfo(currentKeyword, currentCategory, currentKeywordCount)) return

        viewModelScope.launch {
            val oneDaysAgo = latestDateFormat(System.currentTimeMillis())
            val thirtyDaysAgo = thirtyDaysAgoDateFormat(System.currentTimeMillis())
            val timeUnit = Constants.DATE

            try {
                _keywordResponseList.value = Event(
                    repository.requestComparisonResult(
                        KeywordRequest(
                            thirtyDaysAgo,
                            oneDaysAgo,
                            timeUnit,
                            categoryCode,
                            currentKeywordList
                        )
                    )
                )
            } catch (e: Exception) {
                _isError.value = Event(R.string.error_api_communication)
            }
        }
    }

    private fun isValidInfo(keyword: String, categoey: String, keywordCount: Int): Boolean {
        if (keyword.isNullOrBlank()) {
            _snackbarText.value = Event(R.string.announce_blank_keyword)
            return true
        } else if (categoey.isNullOrBlank()) {
            _snackbarText.value = Event(R.string.announce_blank_category)
            return true
        } else if (keywordCount > 5) {
            _snackbarText.value = Event(R.string.keyword_count_over)
            return true
        }
        return false
    }

    companion object {
        fun provideFactory(repository: TrendComparisonRepository) = viewModelFactory {
            initializer {
                TrendComparisonViewModel(repository)
            }
        }
    }
}