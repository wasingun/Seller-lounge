package com.wasingun.seller_lounge.extensions

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation

@BindingAdapter("setRoundCornerImage")
fun ImageView.setRoundedCornerImage(imageAddress: Any?) {
    if (imageAddress != null && (imageAddress is Uri || imageAddress is String)) {
        load(imageAddress) {
            transformations(
                RoundedCornersTransformation(
                    10.0f
                )
            )
            error(null)
        }
    }
}

fun ImageView.setImageList(imageUrl: String?) {
    if (!imageUrl.isNullOrBlank()) {
        load(imageUrl)
    }
}

@BindingAdapter("setCircleImage")
fun ImageView.setCircleImage(imageUrl: String?) {
    if (!imageUrl.isNullOrBlank()) {
        load(imageUrl) {
            transformations(CircleCropTransformation())
        }
    }
}