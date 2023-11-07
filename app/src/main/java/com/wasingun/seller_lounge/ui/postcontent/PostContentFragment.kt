package com.wasingun.seller_lounge.ui.postcontent

import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.wasingun.seller_lounge.R
import com.wasingun.seller_lounge.data.model.DocumentContent
import com.wasingun.seller_lounge.data.model.ImageContent
import com.wasingun.seller_lounge.databinding.FragmentPostContentBinding
import com.wasingun.seller_lounge.extensions.showTextMessage
import com.wasingun.seller_lounge.ui.BaseFragment
import com.wasingun.seller_lounge.util.EventObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostContentFragment: BaseFragment<FragmentPostContentBinding>() {

    private val viewModel by viewModels<PostContentViewModel>()
    private val getImageContents = registerForImage()
    private val getDocumentContents = registerForDocuments()
    private val imageListAdapter = ImageContentAdapter(ImageDeleteListener{imageContent ->
        viewModel.removeImageList(imageContent)
    })
    private val documentListAdapter = DocumentContentAdapter(DocumentDeleteListener {documentContent ->
        viewModel.removeDocumentList(documentContent)
    })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageRequestListener = ImageRequestListener {
            getImageContents()
        }
        binding.documentRequestListener = DocumentRequestListener {
            getDocumentContents()
        }
        binding.rvImageList.adapter = imageListAdapter
        binding.rvDocumentList.adapter = documentListAdapter

        viewModel.imageList.observe(viewLifecycleOwner) { imageContentList ->
            imageListAdapter.submitList(imageContentList)
        }
        viewModel.documentList.observe(viewLifecycleOwner) {documentContentList ->
            documentListAdapter.submitList(documentContentList)
        }
        viewModel.isError.observe(viewLifecycleOwner,EventObserver{resourceId ->
            if (resourceId == R.string.announce_image_attachment_limit) {
                binding.btnAttachmentPhoto.showTextMessage(R.string.announce_image_attachment_limit)
            } else {
                binding.btnAttachmentDocument.showTextMessage(R.string.announce_document_attachment_limit)
            }
        })
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