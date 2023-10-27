package com.wasingun.seller_lounge.ui.trendcomparison

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.wasingun.seller_lounge.R
import com.wasingun.seller_lounge.data.model.trendcomparison.KeywordDetail
import com.wasingun.seller_lounge.data.model.trendcomparison.KeywordRequest
import com.wasingun.seller_lounge.data.repository.TrendComparisonRepository
import com.wasingun.seller_lounge.util.Event
import com.wasingun.seller_lounge.util.latestDateFormat
import com.wasingun.seller_lounge.util.thirtyDaysAgoDateFormat
import kotlinx.coroutines.launch

class TrendComparisonViewModel(private val repository: TrendComparisonRepository) : ViewModel() {

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText
    private val _moveToScreen = MutableLiveData(Event(false))
    val moveToScreen: LiveData<Event<Boolean>> = _moveToScreen
    private val _isError = MutableLiveData(Event(false))
    val isError: LiveData<Event<Boolean>> = _isError
    val keyword = MutableLiveData<String>()
    val category = MutableLiveData<String>()

    fun requestResult() {
        val currentKeyword = keyword.value ?: ""
        val currentCategory = category.value ?: ""
        val currentKeywordCount = currentKeyword?.split(",")?.size ?: 0
        Log.i("error check", "${currentKeyword}, ${currentCategory}, ${currentKeywordCount}")
        if (isValidInfo(currentKeyword, currentCategory, currentKeywordCount)) return
        viewModelScope.launch {
            val oneDaysAgo = latestDateFormat(System.currentTimeMillis())
            val thirtyDaysAgo = thirtyDaysAgoDateFormat(System.currentTimeMillis())
            val timeUnit = "date"

            try {
                repository.requestComparisonResult(
                    KeywordRequest(
                        oneDaysAgo,
                        thirtyDaysAgo,
                        timeUnit,
                        category.value!!,
                        listOf(KeywordDetail(keyword.value!!, listOf(keyword.value!!)))
                    )
                )
                _moveToScreen.value = Event(true)
            } catch(e: Exception) {
                _isError.value = Event(true)
            }
        }
    }

    private fun isValidInfo(keyword: String, categoey: String, keywordCount: Int): Boolean {
        if (keyword.isNullOrBlank()) {
            Log.i("keyword error", "$keyword")
            _snackbarText.value = Event(R.string.announce_blank_keyword)
            return true
        } else if (categoey.isNullOrBlank()) {
            Log.i("category error", "$categoey")
            _snackbarText.value = Event(R.string.announce_blank_category)
            return true
        } else if (keywordCount > 5) {
            Log.i("count error", "$keywordCount")
            _snackbarText.value = Event(R.string.keyword_count_over)
            return true
        }
        return false
    }

    fun resetErrorState() {
        _isError.value = Event(false)
    }

    fun resetMoveToScreenState() {
        _moveToScreen.value = Event(false)
    }

    companion object {
        fun provideFactory(repository: TrendComparisonRepository) = viewModelFactory {
            initializer {
                TrendComparisonViewModel(repository)
            }
        }
    }
}