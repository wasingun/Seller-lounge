package com.wasingun.seller_lounge.data.datasource

import com.google.firebase.storage.FirebaseStorage
import com.wasingun.seller_lounge.data.enums.ProductCategory
import com.wasingun.seller_lounge.data.model.DocumentContent
import com.wasingun.seller_lounge.data.model.ImageContent
import com.wasingun.seller_lounge.data.model.post.PostInfo
import com.wasingun.seller_lounge.data.model.post.UserInfo
import com.wasingun.seller_lounge.network.ApiResponse
import com.wasingun.seller_lounge.network.ApiResultException
import com.wasingun.seller_lounge.network.PostDataClient
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class PostRemoteDataSource @Inject constructor(private val postDataClient: PostDataClient) :
    PostDataSource {

    override suspend fun postInfoUpload(
        postId: String,
        category: ProductCategory,
        title: String,
        body: String,
        imageList: List<ImageContent>,
        documentList: List<DocumentContent>,
        createdTime: String,
        userId: String
    ): ApiResponse<Unit> {

        val imageLocationList = imageList.map { imageContent ->
            getImageFileLocation(userId, imageContent)
        }
        val documentLocationList = documentList.map { documentContent ->
            getDocumentFileLocation(userId, documentContent)
        }

        try {
            withTimeout(10000L) {
                imageList.forEach { imageContent ->
                    async {
                        val storageRef = FirebaseStorage.getInstance().reference
                        val location = getImageFileLocation(userId, imageContent)
                        val imageRef = storageRef.child(location)
                        imageRef.putFile(imageContent.uri).await()
                        location
                    }
                }
                documentList.forEach { documentContent ->
                    async {
                        val storageRef = FirebaseStorage.getInstance().reference
                        val location = getDocumentFileLocation(userId, documentContent)
                        val documentRef = storageRef.child(location)
                        documentRef.putFile(documentContent.uri).await()
                        location
                    }
                }
            }
        } catch (e: Exception) {
            return ApiResultException(Throwable())
        }

        val postInfo = PostInfo(
            postId,
            category,
            title,
            body,
            imageLocationList,
            documentLocationList,
            createdTime,
            userId
        )
        return postDataClient.uploadPostContent(postId, postInfo)
    }

    private fun getDocumentFileLocation(
        userId: String,
        documentContent: DocumentContent
    ) = "documents/${userId}/${documentContent.fileName}"

    private fun getImageFileLocation(
        userId: String,
        imageContent: ImageContent
    ) = "images/${userId}/${imageContent.fileName}"

    override suspend fun userInfoUpload(userId: String, userInfo: UserInfo): ApiResponse<Unit> {
        return postDataClient.uploadUserInfo(userId, userInfo)
    }
}