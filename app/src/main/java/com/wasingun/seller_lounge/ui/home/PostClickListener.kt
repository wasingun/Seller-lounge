package com.wasingun.seller_lounge.ui.home

import com.wasingun.seller_lounge.data.model.post.PostInfo

fun interface PostClickListener {

    fun clickPost(postInfo: PostInfo)
}