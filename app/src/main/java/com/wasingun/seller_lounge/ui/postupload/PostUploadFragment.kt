package com.wasingun.seller_lounge.ui.postupload

import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.wasingun.seller_lounge.R
import com.wasingun.seller_lounge.data.model.ProductCategory
import com.wasingun.seller_lounge.data.model.attachedcontent.DocumentContent
import com.wasingun.seller_lounge.data.model.attachedcontent.ImageContent
import com.wasingun.seller_lounge.databinding.FragmentPostUploadBinding
import com.wasingun.seller_lounge.extensions.showTextMessage
import com.wasingun.seller_lounge.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostUploadFragment : BaseFragment<FragmentPostUploadBinding>() {
    private val viewModel by viewModels<PostUploadViewModel>()
    private val getImageContents = registerForImage()
    private val getDocumentContents = registerForDocuments()
    private val imageListAdapter = ImageContentAdapter(ImageDeleteListener { imageContent ->
        viewModel.removeImageList(imageContent)
    })
    private val documentListAdapter =
        DocumentContentAdapter(DocumentDeleteListener { documentContent ->
            viewModel.removeDocumentList(documentContent)
        })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        setDropdownMenu()
        setLayout()
        setErrorMessage()
    }

    private fun setErrorMessage() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                val announceMessageList = listOf(R.string.announce_image_attachment_limit,
                    R.string.announce_document_attachment_limit,
                    R.string.announce_input_title,
                    R.string.announce_blank_category,
                    R.string.announce_blank_body,
                    R.string.announce_duplicate_file)
                viewModel.isInputError.collect { resourceId ->
                    if (announceMessageList.contains(resourceId)) {
                        binding.root.showTextMessage(resourceId)
                        viewModel.resetInputError()
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isNetworkError.collect { resourceId ->
                    when (resourceId) {
                        R.string.error_api_http_response -> {
                            delay(300)
                            findNavController().navigateUp()
                            binding.root.showTextMessage(R.string.error_api_http_response)
                            viewModel.resetNetworkErrorState()
                        }

                        R.string.error_api_network -> {
                            delay(300)
                            findNavController().navigateUp()
                            binding.root.showTextMessage(R.string.error_api_network)
                            viewModel.resetNetworkErrorState()
                        }
                    }
                }
            }
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.postImageList.collect { imageContentList ->
                    imageListAdapter.submitList(imageContentList)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.postDocumentList.collect { documentContentList ->
                    documentListAdapter.submitList(documentContentList)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isCompleted.collect {
                    if (it) {
                        findNavController().navigateUp()
                        val action = PostUploadFragmentDirections.actionDestPostUploadToDestHome()
                        findNavController().navigate(action)
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoading.collect {
                    if (it) {
                        val action =
                            PostUploadFragmentDirections.actionDestPostUploadToDestLoadingDialog()
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    private fun setLayout() {
        binding.imageRequestListener = ImageRequestListener {
            getImageContents()
        }
        binding.documentRequestListener = DocumentRequestListener {
            getDocumentContents()
        }
        binding.viewModel = viewModel
        binding.rvImageList.adapter = imageListAdapter
        binding.rvDocumentList.adapter = documentListAdapter
        binding.rvImageList.setHasFixedSize(true)
        binding.rvDocumentList.setHasFixedSize(true)
    }

    private fun setDropdownMenu() {
        val categoryList = ProductCategory.values().map { it.categoryName }.slice(1..10)
        val categoryArrayAdapter =
            ArrayAdapter(requireContext(), R.layout.layout_dropdown, categoryList)
        binding.actvCategoryList.setAdapter(categoryArrayAdapter)
        binding.actvCategoryList.setDropDownBackgroundDrawable(
            ColorDrawable(ContextCompat.getColor(requireContext(), R.color.white))
        )
    }

    private fun registerForImage() =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(10)) { imageUriList ->
            val imageList = mutableListOf<ImageContent>()
            for (uri in imageUriList) {
                val fileName = getFileName(uri)
                imageList.add(ImageContent(uri, fileName))
            }
            viewModel.addImageList(imageList)
        }

    private fun registerForDocuments() =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { documentUri ->
            val uri = documentUri ?: Uri.EMPTY
            val fileName = documentUri?.let { getFileName(it) } ?: ""
            if (uri != Uri.EMPTY) {
                viewModel.addDocumentList(DocumentContent(uri, fileName))
            }
        }

    private fun getImageContents() {
        getImageContents.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun getDocumentContents() {
        getDocumentContents.launch(
            arrayOf(
                "application/pdf",
                "application/vnd.ms-excel",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "application/msword",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "application/vnd.ms-powerpoint",
                "application/vnd.openxmlformats-officedocument.presentationml.presentation",
                "application/zip",
            )
        )
    }

    private fun getFileName(uri: Uri): String {
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        return cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            it.moveToFirst()
            it.getString(nameIndex)
        } ?: ""
    }

    override fun getFragmentView(): Int {
        return R.layout.fragment_post_upload
    }
}