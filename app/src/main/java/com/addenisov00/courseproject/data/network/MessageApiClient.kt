package com.addenisov00.courseproject.data.network

import com.addenisov00.courseproject.data.network.models.*
import okhttp3.MultipartBody
import retrofit2.http.*


interface MessageApiClient {
    @GET("users")
    suspend fun getAllUsers(): UsersResponse

    @GET("realm/presence")
    suspend fun getAllUserStatuses(): AllPeopleStatusResponse

    @GET("users/{user_id}")
    suspend fun getUser(@Path("user_id") id: Int): UserResponse


    @GET("users/{user_id}/presence")
    suspend fun getUserStatus(@Path("user_id") id: Int): UserStatusResponse

    @GET("users/me/subscriptions")
    suspend fun getMyChannels(): MyChannelsResponse

    @GET("streams")
    suspend fun getAllChannels(): AllChannelsResponse


    @GET("users/me/{stream_id}/topics")
    suspend fun getTopics(@Path("stream_id") id: Int): TopicsResponseModel


    @GET("messages")
    suspend fun getMessagesWithFilter(
        @Query("num_before") numBefore: Int,
        @Query("num_after") numAfter: Int,
        @Query("anchor") anchor: Any,
        @Query("narrow") narrow: String,
    ): GetMessagesResponse


    @GET("messages/{message_id}")
    suspend fun getMessage(@Path("message_id") messageId: Int): GetOneMessageResponse


    @DELETE("messages/{message_id}")
    suspend fun deleteMessage(@Path("message_id") messageId: Int): PostResult


    @PATCH("messages/{message_id}")
    suspend fun editMessage(
        @Path("message_id") messageId: Int,
        @Query("content") newContent: String
    ): PostResult

    @POST("messages/{message_id}/reactions")
    suspend fun addReaction(
        @Path("message_id") messageId: Int,
        @Query("emoji_name") emojiName: String
    ): PostResult

    @DELETE("messages/{message_id}/reactions")
    suspend fun deleteReaction(
        @Path("message_id") messageId: Int,
        @Query("emoji_name") emojiName: String
    ): PostResult

    @POST("messages")
    suspend fun sendMessageToTopic(
        @Query("type") type: String,
        @Query("to") receiverId: Int,
        @Query("content") content: String,
        @Query("topic") topic: String = "stream"
    ): SendMessageResult


    @POST("users/me/subscriptions")
    suspend fun createChannel(
        @Query("subscriptions") channelInfo: String
    ): CreateChannelResponse


    @POST("user_uploads")
    suspend fun addPhoto(
        @Body photo: MultipartBody
    ): AddPhotoResponse

}