package com.wasingun.seller_lounge.ui.common

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.wasingun.seller_lounge.databinding.DialogFragmentLoadingBinding
import com.wasingun.seller_lounge.extensions.setDialogSize

class LoadingDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DialogFragmentLoadingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            setOnKeyListener { _, keyCode, event ->
                return@setOnKeyListener keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP
            }
        }
    }

    override fun onResume() {
        super.onResume()
        context?.setDialogSize(this, 0.9f, 0.2f)
    }
}