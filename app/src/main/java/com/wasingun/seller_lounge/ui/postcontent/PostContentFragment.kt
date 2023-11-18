package com.wasingun.seller_lounge.ui.postcontent

import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.wasingun.seller_lounge.R
import com.wasingun.seller_lounge.data.enums.ProductCategory
import com.wasingun.seller_lounge.data.model.DocumentContent
import com.wasingun.seller_lounge.data.model.ImageContent
import com.wasingun.seller_lounge.databinding.FragmentPostContentBinding
import com.wasingun.seller_lounge.extensions.showTextMessage
import com.wasingun.seller_lounge.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostContentFragment : BaseFragment<FragmentPostContentBinding>() {

    private val viewModel by viewModels<PostContentViewModel>()
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
        lifecycleScope.launch {
            viewModel.isInputError.collect { resourceId ->
                when (resourceId) {
                    R.string.announce_image_attachment_limit -> {
                        binding.btnAttachmentPhoto.showTextMessage(R.string.announce_image_attachment_limit)
                    }

                    R.string.announce_document_attachment_limit -> {
                        binding.btnAttachmentDocument.showTextMessage(R.string.announce_document_attachment_limit)
                    }

                    R.string.announce_input_title -> {
                        binding.btnComplete.showTextMessage(R.string.announce_input_title)
                    }

                    R.string.announce_blank_category -> {
                        binding.btnComplete.showTextMessage(R.string.announce_blank_category)
                    }

                    R.string.announce_blank_body -> {
                        binding.btnComplete.showTextMessage(R.string.announce_blank_body)
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewModel.isNetworkError.collect() { resourceId ->
                when (resourceId) {
                    R.string.error_api_http_response -> {
                        delay(300)
                        findNavController().navigateUp()
                        binding.root.showTextMessage(R.string.error_api_http_response)
                    }

                    R.string.error_api_network -> {
                        delay(300)
                        findNavController().navigateUp()
                        binding.root.showTextMessage(R.string.error_api_network)
                    }
                }
            }
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewModel.postImageList.collect { imageContentList ->
                imageListAdapter.submitList(imageContentList)
            }
        }
        lifecycleScope.launch {
            viewModel.postDocumentList.collect { documentContentList ->
                documentListAdapter.submitList(documentContentList)
            }
        }
        lifecycleScope.launch {
            viewModel.isCompleted.collect {
                if (it) {
                    delay(300)
                    findNavController().navigateUp()
                    findNavController().navigateUp()
                }
            }
        }
        lifecycleScope.launch {
            viewModel.isLoading.collect {
                if (it) {
                    val action =
                        PostContentFragmentDirections.actionDestPostContentToLoadingDialogFragment()
                    findNavController().navigate(action)
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
    }

    private fun setDropdownMenu() {
        val categoryList = ProductCategory.values().map { it.categoryName }.take(10)
        val categoryArrayAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_sort, categoryList)
        binding.actvCategoryList.setAdapter(categoryArrayAdapter)
        binding.actvCategoryList.setDropDownBackgroundDrawable(
            ColorDrawable(ContextCompat.getColor(requireContext(), R.color.white))
        )
    }

    private fun registerForImage() =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { imageUriList ->
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
        getImageContents.launch("image/*")
    }

    private fun getDocumentContents() {
        getDocumentContents.launch(
            arrayOf(
                "application/pdf",
                "application/msword",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "application/zip",
                "application/vnd.ms-powerpoint",
                "application/vnd.openxmlformats-officedocument.presentationml.presentation"
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
        return R.layout.fragment_post_content
    }
}