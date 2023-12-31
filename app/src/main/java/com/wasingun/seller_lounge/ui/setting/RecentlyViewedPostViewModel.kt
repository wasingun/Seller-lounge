package com.wasingun.seller_lounge.ui.setting

import androidx.lifecycle.ViewModel
import com.wasingun.seller_lounge.data.model.post.PostInfo
import com.wasingun.seller_lounge.data.repository.RecentPostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecentlyViewedPostViewModel @Inject constructor(private val repository: RecentPostRepository) :
    ViewModel() {

    suspend fun getLocalPostList(): List<PostInfo> {
        return repository.getLocalPostList()
    }
}