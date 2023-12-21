package com.wasingun.seller_lounge.ui.postupload

import com.wasingun.seller_lounge.data.model.attachedcontent.DocumentContent

fun interface DocumentDeleteListener {

    fun clickDocumentDelete(documentContent: DocumentContent)
}