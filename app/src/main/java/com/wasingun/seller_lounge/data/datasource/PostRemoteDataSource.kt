package com.wasingun.seller_lounge.data.datasource

import com.google.firebase.storage.FirebaseStorage
import com.wasingun.seller_lounge.data.enums.ProductCategory
import com.wasingun.seller_lounge.data.model.DocumentContent
import com.wasingun.seller_lounge.data.model.ImageContent
import com.wasingun.seller_lounge.data.model.post.PostInfo
import com.wasingun.seller_lounge.data.model.post.UserInfo
import com.wasingun.seller_lounge.network.ApiResponse
import com.wasingun.seller_lounge.network.PostDataClient
import kotlinx.coroutines.tasks.await
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
            val storageRef = FirebaseStorage.getInstance().reference
            val location = "images/${userId}/${imageContent.fileName}"
            val imageRef = storageRef.child(location)
            imageRef.putFile(imageContent.uri).await()
            location
        }

        val documentLocationList = documentList.map { documentContent ->
            val storageRef = FirebaseStorage.getInstance().reference
            val location = "documents/${userId}/${documentContent.fileName}"
            val documentRef = storageRef.child(location)
            documentRef.putFile(documentContent.uri).await()
            location
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

    override suspend fun userInfoUpload(userId: String, userInfo: UserInfo): ApiResponse<Unit> {
        return postDataClient.uploadUserInfo(userId, userInfo)
    }
}