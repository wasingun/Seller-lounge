package com.wasingun.seller_lounge.ui.postcontent

import com.wasingun.seller_lounge.data.model.attachedcontent.ImageContent

fun interface ImageDeleteListener {

    fun clickImageDelete(imageContent: ImageContent)
}