package com.wasingun.seller_lounge.ui.postdetail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.storage.FirebaseStorage
import com.wasingun.seller_lounge.R
import com.wasingun.seller_lounge.data.model.post.PostInfo
import com.wasingun.seller_lounge.databinding.FragmentPostDetailBinding
import com.wasingun.seller_lounge.extensions.setCircleImage
import com.wasingun.seller_lounge.extensions.showTextMessage
import com.wasingun.seller_lounge.workmanager.CoroutineDownloadWorker
import com.wasingun.seller_lounge.ui.BaseFragment
import com.wasingun.seller_lounge.util.Constants
import com.wasingun.seller_lounge.util.convertDisplayedDate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@AndroidEntryPoint
class PostDetailFragment : BaseFragment<FragmentPostDetailBinding>() {
    private val args: PostDetailFragmentArgs by navArgs()
    private val postImageAdapter = PostImageAdapter()
    private lateinit var documentAdapter: PostDetailAttachedDocumentAdapter
    private val viewModel: PostDetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val postInfo = args.post
        documentAdapter = PostDetailAttachedDocumentAdapter(AttachedFileClickListener { fileName ->
            startDownloadDocument(postInfo, fileName)
        })
        getWriterInfo()
        setLayout(postInfo)
        setWriterInfo()
        saveLocalPost(postInfo)
        setErrorMessage()
        submitDocumentFileName(postInfo)
    }

    private fun startDownloadDocument(
        postInfo: PostInfo,
        fileName: String?
    ) {
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
        lifecycleScope.launch {
            viewModel.isError.collect { errorMessage ->
                if (errorMessage != 0) {
                    binding.root.showTextMessage(errorMessage)
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
        lifecycleScope.launch {
            viewModel.writerInfo.collect { writerInfo ->
                binding.tvUserName.text = writerInfo.userName
                binding.ivProfileImage.setCircleImage(writerInfo.userImage)
            }
        }
    }

    private fun setLayout(postInfo: PostInfo) {
        with(binding) {
            tvTitle.text = postInfo.title
            tvCategory.text = postInfo.category.categoryName
            tvBody.text = postInfo.body
            tvTime.convertDisplayedDate(postInfo.createTime)
            vpImageList.adapter = postImageAdapter
            btnArrow.setOnClickListener {
                findNavController().navigateUp()
            }
        }
        if (postInfo.imageList.isNullOrEmpty()) {
            binding.vpImageList.visibility = View.GONE
        } else {
            postImageAdapter.submitList(postInfo.imageList)
        }

        TabLayoutMediator(binding.tlCircleIndicator, binding.vpImageList) { tab, position ->

        }.attach()
    }

    override fun getFragmentView(): Int {
        return R.layout.fragment_post_detail
    }
}