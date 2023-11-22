package com.wasingun.seller_lounge.extensions

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.transform.RoundedCornersTransformation

@BindingAdapter("setRoundCornerImage")
fun ImageView.setRoundedCornerImage(imageAddress: Any?) {
    if (imageAddress != null) {
        when(imageAddress) {
            is Uri -> {
                load(imageAddress) {
                    transformations(
                        RoundedCornersTransformation(
                            10.0f
                        )
                    )
                    error(null)
                }
            }
            is String -> {
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
    }
}

fun ImageView.setImageList(imageUrl: String?) {
    if (!imageUrl.isNullOrBlank()) {
        load(imageUrl)
    }
}