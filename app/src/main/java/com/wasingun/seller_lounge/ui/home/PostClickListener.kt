package com.wasingun.seller_lounge.ui.home

import com.wasingun.seller_lounge.data.model.ImageContent

fun interface PostClickListener {

    fun clickPost(imageContent: ImageContent)
}