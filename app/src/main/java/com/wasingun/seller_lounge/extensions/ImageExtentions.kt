package com.wasingun.seller_lounge.extensions

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.transform.RoundedCornersTransformation

@BindingAdapter("setRoundCornerImage")
fun ImageView.setRoundedCornerImage(uri: Uri) {
    load(uri) {
        transformations(
            RoundedCornersTransformation(
                10.0f
            )
        )
        error(null)
    }
}