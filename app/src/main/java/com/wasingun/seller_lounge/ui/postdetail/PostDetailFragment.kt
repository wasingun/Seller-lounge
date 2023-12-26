package com.wasingun.seller_lounge.ui.postdetail

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import com.wasingun.seller_lounge.R
import com.wasingun.seller_lounge.SellerLoungeApplication
import com.wasingun.seller_lounge.data.model.post.PostInfo
import com.wasingun.seller_lounge.data.preferencemanager.DataStore
import com.wasingun.seller_lounge.databinding.FragmentPostDetailBinding
import com.wasingun.seller_lounge.extensions.setCircleImage
import com.wasingun.seller_lounge.extensions.showTextMessage
import com.wasingun.seller_lounge.workmanager.CoroutineDownloadWorker
import com.wasingun.seller_lounge.ui.BaseFragment
import com.wasingun.seller_lounge.constants.Constants
import com.wasingun.seller_lounge.util.convertDisplayedDate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@AndroidEntryPoint
class PostDetailFragment : BaseFragment<FragmentPostDetailBinding>() {
    private val args: PostDetailFragmentArgs by navArgs()
    private val postImageAdapter = PostImageAdapter()
    private lateinit var documentAdapter: PostDetailAttachedDocumentAdapter
    private val viewModel: PostDetailViewModel by viewModels()
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissionLauncher =
            setNotificationPermissionMessage()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val postInfo = args.post
        documentAdapter = PostDetailAttachedDocumentAdapter(AttachedFileClickListener { fileName ->
            setDownloadAlertDialog(postInfo, fileName)
        })
        val userInfo = viewModel.getUserInfo()
        getWriterInfo()
        setLayout(postInfo, userInfo)
        setWriterInfo()
        saveLocalPost(postInfo)
        setErrorMessage()
        submitDocumentFileName(postInfo)
        updateButtonVisibility(userInfo)
        collectLoadingState()
        collectCompletedState()
        collectNetworkRequestErrorState()
        binding.tvEdit.setOnClickListener {
            val action = PostDetailFragmentDirections.actionDestPostDetailToDestEditPost(postInfo)
            findNavController().navigate(action)
        }
    }

    private fun collectNetworkRequestErrorState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isNetworkRequestError.collect { resourceId ->
                    val announceMessageList = listOf(
                        R.string.error_api_network,
                        R.string.error_api_http_response
                    )
                    if (announceMessageList.contains(resourceId)) {
                        delay(300)
                        findNavController().navigateUp()
                        binding.root.showTextMessage(resourceId)
                        viewModel.resetNetworkRequestErrorState()
                    }
                }
            }
        }
    }

    private fun collectCompletedState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isCompleted.collect {
                    if (it) {
                        delay(300)
                        val action = PostDetailFragmentDirections.actionDestPostDetailToDestHome()
                        findNavController().navigateUp()
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    private fun collectLoadingState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoading.collect {
                    if (it) {
                        val action =
                            PostDetailFragmentDirections.actionDestPostDetailToDestLoadingDialog()
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    private fun updateButtonVisibility(userInfo: FirebaseUser?) {
        if (userInfo?.uid == args.post.userId) {
            binding.tvDelete.visibility = View.VISIBLE
            binding.tvEdit.visibility = View.VISIBLE
        }
    }

    private fun setDownloadAlertDialog(
        postInfo: PostInfo,
        fileName: String?
    ) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.document_download)
            .setMessage(R.string.announce_document_download)
            .setPositiveButton(
                R.string.yes
            ) { dialog, which -> startDownloadDocument(postInfo, fileName) }
            .setNegativeButton(R.string.no) { _, _ -> }
            .show()
    }

    private fun startDownloadDocument(
        postInfo: PostInfo,
        fileName: String?
    ) {
        requestStoragePermission()
        requestNotificationPermission()

        binding.root.showTextMessage(R.string.announce_download_start)
        lifecycleScope.launch {
            val storageUrl =
                "documents/${postInfo.userId}/" + fileName
            val workManager = WorkManager.getInstance(requireContext())
            val constraints = Constraints.Builder().build()
            val downloadUrl = getDownloadUrl(storageUrl)
            val data = Data.Builder().putString(
                Constants.KEY_DOCUMENT_URL, downloadUrl
            ).putString(
                Constants.KEY_FILE_NAME, fileName
            ).build()
            val work = OneTimeWorkRequestBuilder<CoroutineDownloadWorker>()
                .setConstraints(constraints)
                .setInputData(data)
                .build()
            val continuation = workManager.beginUniqueWork(
                "save_document",
                ExistingWorkPolicy.APPEND_OR_REPLACE,
                work
            )
            continuation.enqueue()
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val permissionCheck = DataStore.permissionCheck
            if (!permissionCheck) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                SellerLoungeApplication.preferenceManager.putBoolean(
                    Constants.KEY_PERMISSION_CHECK,
                    true
                )
            }
        }
    }

    private fun setNotificationPermissionMessage() =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            when (it) {
                true -> {
                    Toast.makeText(
                        context,
                        getString(R.string.download_permission_admit),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                false -> {
                    Toast.makeText(
                        context,
                        getString(R.string.download_permission_deny),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    private fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            val permissionList = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
            )
            if (!permissionList.all {
                    ContextCompat.checkSelfPermission(
                        requireContext(),
                        it
                    ) == PackageManager.PERMISSION_GRANTED
                }) {
                requestPermissions(permissionList, Constants.REQUEST_PERMISSION_CODE)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Constants.REQUEST_PERMISSION_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    binding.root.showTextMessage(R.string.permission_allow)
                } else {
                    binding.root.showTextMessage(R.string.permission_deny)
                }
            }
        }
    }

    private suspend fun getDownloadUrl(location: String): String {
        val firebaseStorage = FirebaseStorage.getInstance()
        return firebaseStorage.getReference(location)
            .downloadUrl
            .await()
            .toString()
    }

    private fun submitDocumentFileName(postInfo: PostInfo) {
        binding.rvDocumentList.adapter = documentAdapter
        val fileNameList = postInfo.documentList?.map {
            it.replace("documents/${postInfo.userId}/", "")
        }
        documentAdapter.submitList(fileNameList)
    }

    private fun setErrorMessage() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isUserInfoLoadError.collect { errorMessage ->
                    if (errorMessage != 0) {
                        binding.root.showTextMessage(errorMessage)
                    }
                }
            }
        }
    }

    private fun saveLocalPost(postInfo: PostInfo) {
        lifecycleScope.launch {
            viewModel.saveLocalPost(postInfo)
        }
    }

    private fun getWriterInfo() {
        lifecycleScope.launch {
            viewModel.getWriterInfo(args.post.userId)
        }
    }

    private fun setWriterInfo() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.writerInfo.collect { writerInfo ->
                    binding.tvUserName.text = writerInfo.userName
                    binding.ivProfileImage.setCircleImage(writerInfo.userImage)
                }
            }
        }
    }

    private fun setLayout(postInfo: PostInfo, userInfo: FirebaseUser?) {
        with(binding) {
            tvTitle.text = postInfo.title
            tvCategory.text = postInfo.category.categoryName
            tvBody.text = postInfo.body
            tvTime.convertDisplayedDate(postInfo.createTime)
            vpImageList.adapter = postImageAdapter
            mtbPostDetail.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            tvDelete.setOnClickListener {
                setDeleteAlertDialog(userInfo)
            }
            rvDocumentList.setHasFixedSize(true)
        }
        if (postInfo.imageList.isNullOrEmpty()) {
            binding.vpImageList.visibility = View.GONE
        } else {
            postImageAdapter.submitList(postInfo.imageList)
        }

        TabLayoutMediator(binding.tlCircleIndicator, binding.vpImageList) { tab, position ->

        }.attach()
    }

    private fun setDeleteAlertDialog(userInfo: FirebaseUser?) {
        if (userInfo?.uid == args.post.userId) {
            AlertDialog.Builder(requireContext())
                .setTitle(R.string.delete_post)
                .setMessage(R.string.announce_delete_post)
                .setPositiveButton(
                    R.string.yes
                ) { dialog, which -> viewModel.deletePost(args.post.postId) }
                .setNegativeButton(R.string.no) { _, _ -> }
                .show()
        }
    }

    override fun getFragmentView(): Int {
        return R.layout.fragment_post_detail
    }
}