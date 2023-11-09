package com.wasingun.seller_lounge.ui.trendcomparison

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wasingun.seller_lounge.R
import com.wasingun.seller_lounge.data.enums.ProductCategory
import com.wasingun.seller_lounge.data.model.trendcomparison.KeywordDetail
import com.wasingun.seller_lounge.data.model.trendcomparison.KeywordRequest
import com.wasingun.seller_lounge.data.model.trendcomparison.KeywordResponse
import com.wasingun.seller_lounge.data.repository.SearchContentRepository
import com.wasingun.seller_lounge.network.onError
import com.wasingun.seller_lounge.network.onException
import com.wasingun.seller_lounge.network.onSuccess
import com.wasingun.seller_lounge.util.Constants
import com.wasingun.seller_lounge.util.Event
import com.wasingun.seller_lounge.util.latestDateFormat
import com.wasingun.seller_lounge.util.thirtyDaysAgoDateFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrendComparisonViewModel @Inject constructor(private val repository: SearchContentRepository) :
    ViewModel() {

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
            .map { KeywordDetail(it, listOf(it)) }
        val currentKeywordCount = currentKeywordList.size

        if (isValidInfo(currentKeyword, currentCategory, currentKeywordCount)) return

        viewModelScope.launch {
            val oneDaysAgo = latestDateFormat(System.currentTimeMillis())
            val thirtyDaysAgo = thirtyDaysAgoDateFormat(System.currentTimeMillis())
            val timeUnit = Constants.DATE

            val result = repository.requestComparisonResult(
                KeywordRequest(
                    thirtyDaysAgo,
                    oneDaysAgo,
                    timeUnit,
                    categoryCode,
                    currentKeywordList
                )
            )
            result.onSuccess {
                _keywordResponseList.value = Event(it)
            }.onError { _, _ ->
                _isError.value = Event(R.string.error_api_http_response)
            }.onException {
                _isError.value = Event(R.string.error_api_network)
            }
        }
    }

    private fun isValidInfo(keyword: String, categoey: String, keywordCount: Int): Boolean {
        if (keyword.isBlank()) {
            _snackbarText.value = Event(R.string.announce_blank_keyword)
            return true
        } else if (categoey.isBlank()) {
            _snackbarText.value = Event(R.string.announce_blank_category)
            return true
        } else if (keywordCount > 5) {
            _snackbarText.value = Event(R.string.keyword_count_over)
            return true
        }
        return false
    }
}