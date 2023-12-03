package com.wasingun.seller_lounge.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.wasingun.seller_lounge.BuildConfig
import com.wasingun.seller_lounge.R
import com.wasingun.seller_lounge.databinding.FragmentLoginBinding
import com.wasingun.seller_lounge.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val viewModel: LoginViewModel by viewModels()
    private lateinit var signInClient: GoogleSignInClient
    private val googleSignInOption =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_CLIENT_ID)
            .requestEmail()
            .build()
    private lateinit var launcher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signInClient = GoogleSignIn.getClient(requireContext(), googleSignInOption)
        launcher = activityResultLauncher()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            signInGoogle()
        }
    }

    private fun signInGoogle() {
        val signInIntent = signInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private fun activityResultLauncher() =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            kotlin.runCatching {
                handleResults(task)
            }.onFailure {
                Snackbar.make(requireView(), R.string.login_fail, Snackbar.LENGTH_SHORT).show()
            }
        }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account = task.result
            if (account != null) {
                updateUi(account)
            }
        } else {
            Snackbar.make(requireView(), R.string.auth_fail, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun updateUi(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        viewModel.getSignInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                val action = LoginFragmentDirections.actionDestLoginToDestHome()
                findNavController().navigate(action)
            } else {
                Snackbar.make(requireView(), R.string.login_fail, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun getFragmentView(): Int {
        return R.layout.fragment_login
    }
}