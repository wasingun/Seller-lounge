package com.wasingun.seller_lounge.data.datasource

import com.google.firebase.storage.FirebaseStorage
import com.wasingun.seller_lounge.data.model.ProductCategory
import com.wasingun.seller_lounge.data.model.attachedcontent.DocumentContent
import com.wasingun.seller_lounge.data.model.attachedcontent.ImageContent
import com.wasingun.seller_lounge.data.model.post.PostInfo
import com.wasingun.seller_lounge.data.model.post.UserInfo
import com.wasingun.seller_lounge.network.ApiResponse
import com.wasingun.seller_lounge.network.ApiResultException
import com.wasingun.seller_lounge.network.PostDataClient
import com.wasingun.seller_lounge.network.onError
import com.wasingun.seller_lounge.network.onException
import com.wasingun.seller_lounge.network.onSuccess
import com.wasingun.seller_lounge.util.Constants
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class PostRemoteDataSource @Inject constructor(private val postDataClient: PostDataClient) :
    PostDataSource {

    override suspend fun uploadPostInfo(
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

        if (uploadAttachedFile(imageList, userId, documentList)) {
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
        return postDataClient.uploadPost(postId, postInfo)
    }

    private suspend fun uploadAttachedFile(
        imageList: List<ImageContent>,
        userId: String,
        documentList: List<DocumentContent>
    ): Boolean {
        if (uploadFile(imageList, userId, documentList)) return true
        return false
    }

    private suspend fun uploadFile(
        imageList: List<ImageContent>,
        userId: String,
        documentList: List<DocumentContent>
    ): Boolean {
        val storageRef = FirebaseStorage.getInstance().reference
        try {
            withTimeout(15000L) {
                imageList.map { imageContent ->
                    async {
                        val location = getImageFileLocation(userId, imageContent)
                        val imageRef = storageRef.child(location)
                        imageRef.putFile(imageContent.uri).await()
                        location
                    }
                } + documentList.map { documentContent ->
                    async {
                        val location = getDocumentFileLocation(userId, documentContent)
                        val documentRef = storageRef.child(location)
                        documentRef.putFile(documentContent.uri).await()
                        location
                    }
                }.awaitAll()
            }
        } catch (e: Exception) {
            return true
        }
        return false
    }

    override fun getPostList(
        onComplete: () -> Unit,
        onError: (String) -> Unit
    ): Flow<List<PostInfo>> {
        return flow {
            val result = postDataClient.getPostList()
            result.onSuccess { postMapCollection ->
                emit(postMapCollection.map { entry ->
                    entry.value.run {
                        this.copy(
                            imageList = imageList?.map {
                                getDownloadUrl(it)
                            }
                        )
                    }
                })
            }.onError { code, message ->
                onError(Constants.REQUEST_ERROR)
            }.onException {
                onError(Constants.NETWORK_ERROR)
            }
        }.onCompletion {
            onComplete()
        }
    }

    override suspend fun uploadUserInfo(userId: String, userInfo: UserInfo): ApiResponse<Unit> {
        return postDataClient.uploadUserInfo(userId, userInfo)
    }

    override suspend fun getWriterInfo(userId: String): ApiResponse<UserInfo> {
        return postDataClient.getUserInfo(userId)
    }

    override suspend fun deletePost(postId: String): ApiResponse<Unit> {
        return postDataClient.deletePost(postId)
    }

    override suspend fun updatePost(postId: String, postInfo: PostInfo): ApiResponse<Unit> {
        return postDataClient.updatePost(postId, postInfo)
    }

    override suspend fun getPostInfo(postId: String): ApiResponse<PostInfo> {
        return postDataClient.getPost(postId)
    }

    private suspend fun getDownloadUrl(location: String): String {
        val firebaseStorage = FirebaseStorage.getInstance()
        return firebaseStorage.getReference(location)
            .downloadUrl
            .await()
            .toString()
    }

    private fun getDocumentFileLocation(
        userId: String,
        documentContent: DocumentContent
    ) = "documents/${userId}/${documentContent.fileName}"

    private fun getImageFileLocation(
        userId: String,
        imageContent: ImageContent
    ) = "images/${userId}/${imageContent.fileName}"
}