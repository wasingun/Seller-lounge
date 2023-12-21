package com.wasingun.seller_lounge.ui.setting

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.wasingun.seller_lounge.BuildConfig
import com.wasingun.seller_lounge.R
import com.wasingun.seller_lounge.databinding.FragmentSettingBinding
import com.wasingun.seller_lounge.ui.BaseFragment
import com.wasingun.seller_lounge.constants.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding>() {
    private val viewModel: SettingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.firebaseUser = viewModel.getCurrentUser()
        binding.tvRecentlyViewedPost.setOnClickListener {
            val action = SettingFragmentDirections.actionDestSettingToDestRecentlyViewedPost()
            findNavController().navigate(action)
        }
        binding.tvCustomerFeedback.setOnClickListener {
            moveFeedbackWebPage()
        }
        binding.tvLogout.setOnClickListener {
            setLogoutAlertDialog()
        }
    }

    private fun moveFeedbackWebPage() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(Constants.CUSTOMER_FEEDBACK)
        startActivity(intent)
    }

    private fun setLogoutAlertDialog() {
        val action = SettingFragmentDirections.actionDestSettingToDestLogin()
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.logout)
            .setMessage(R.string.logout_message)
            .setPositiveButton(
                R.string.yes
            ) { dialog, which ->
                val firebaseAuth = FirebaseAuth.getInstance()
                val googleSignInOption =
                    GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(BuildConfig.GOOGLE_CLIENT_ID)
                        .requestEmail()
                        .build()
                val googleSignInClient = GoogleSignIn.getClient(requireContext(),googleSignInOption)
                firebaseAuth.signOut()
                googleSignInClient.signOut()
                findNavController().navigate(action) }
            .setNegativeButton(R.string.no) { _, _ -> }
            .show()
    }

    override fun getFragmentView(): Int {
        return R.layout.fragment_setting
    }
}