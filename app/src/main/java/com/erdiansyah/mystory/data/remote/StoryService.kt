package com.erdiansyah.mystory.data.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface StoryService {
    @GET("v1/stories")
    suspend fun getStory(
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null
    ) : Response<StoryResponse>

    @GET("v1/stories?location=1")
    suspend fun getStoryLocation(
    ) : Response<StoryLocationResponse>

    @Multipart
    @POST("v1/stories")
    suspend fun postStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part ("lat") lat: Double?,
        @Part ("lon") lon: Double?
    ):Response<PostStoryResponse>
}