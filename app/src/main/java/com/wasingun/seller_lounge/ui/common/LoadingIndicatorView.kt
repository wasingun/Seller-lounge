package com.wasingun.seller_lounge.ui.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.wasingun.seller_lounge.databinding.ViewLoadingIndicatorBinding

class LoadingIndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    init {
        ViewLoadingIndicatorBinding.inflate(LayoutInflater.from(context), this)
    }
}