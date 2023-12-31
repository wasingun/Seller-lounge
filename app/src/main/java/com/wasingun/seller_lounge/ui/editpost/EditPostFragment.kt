package com.wasingun.seller_lounge.ui.editpost

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.wasingun.seller_lounge.R
import com.wasingun.seller_lounge.data.model.ProductCategory
import com.wasingun.seller_lounge.data.model.post.PostInfo
import com.wasingun.seller_lounge.databinding.FragmentEditPostBinding
import com.wasingun.seller_lounge.extensions.showTextMessage
import com.wasingun.seller_lounge.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditPostFragment : BaseFragment<FragmentEditPostBinding>() {
    private val args: EditPostFragmentArgs by navArgs()
    private val viewModel: EditPostViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val postInfo = args.post
        val postId = postInfo.postId
        setWrittenPost(postInfo)
        setDropdownMenu()
        setCompleteButtonAction(postId)
        collectLoadingState()
        collectCompleteState()
        collectNetworkErrorState()
        setInputErrorState()
    }

    private fun setInputErrorState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                val announceMessageList = listOf(
                    R.string.announce_image_attachment_limit,
                    R.string.announce_document_attachment_limit,
                    R.string.announce_input_title,
                    R.string.announce_blank_category,
                    R.string.announce_blank_body,
                    R.string.announce_duplicate_file
                )
                viewModel.isInputError.collect { resourceId ->
                    if (announceMessageList.contains(resourceId)) {
                        binding.root.showTextMessage(resourceId)
                        viewModel.resetInputErrorState()
                    }
                }
            }
        }
    }

    private fun collectNetworkErrorState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                val announceMessageList = listOf(
                    R.string.error_api_http_response,
                    R.string.error_api_network
                )
                viewModel.isNetworkError.collect { resourceId ->
                    if (announceMessageList.contains(resourceId)) {
                        delay(300)
                        findNavController().navigateUp()
                        binding.root.showTextMessage(resourceId)
                        viewModel.resetNetworkErrorState()
                    }
                }
            }
        }
    }

    private fun collectCompleteState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isCompleted.collect { completeState ->
                    if (completeState) {
                        findNavController().navigateUp()
                        delay(300)
                        val action = EditPostFragmentDirections.actionDestEditPostToDestHome()
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    private fun collectLoadingState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoading.collect { loadingState ->
                    if (loadingState) {
                        val action = EditPostFragmentDirections.actionDestEditPostToDestLoadingDialog()
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    private fun setCompleteButtonAction(postId: String) {
        binding.btnComplete.setOnClickListener {
            val editedCategoryName = binding.actvCategoryList.text.toString()
            val editedCategory = ProductCategory.values().firstOrNull() {
                editedCategoryName == it.categoryName
            } ?: ProductCategory.ALL
            val editedTitle = binding.etTitle.text.toString()
            val editedBody = binding.etBody.text.toString()
            viewModel.updatePost(postId, editedCategory, editedTitle, editedBody)
        }
    }

    private fun setWrittenPost(postInfo: PostInfo) {
        with(binding) {
            actvCategoryList.setText(postInfo.category.categoryName)
            etBody.setText(postInfo.body)
            etTitle.setText(postInfo.title)
        }
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

    override fun getFragmentView(): Int {
        return R.layout.fragment_edit_post
    }
}