package com.wasingun.seller_lounge.ui.postcontent

import com.wasingun.seller_lounge.data.model.DocumentContent

fun interface DocumentDeleteListener {

    fun clickDocumentDelete(documentContent: DocumentContent)
}