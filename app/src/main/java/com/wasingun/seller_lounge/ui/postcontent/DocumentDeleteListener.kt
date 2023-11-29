package com.wasingun.seller_lounge.ui.postcontent

import com.wasingun.seller_lounge.data.model.attachedcontent.DocumentContent

fun interface DocumentDeleteListener {

    fun clickDocumentDelete(documentContent: DocumentContent)
}