package com.erdiansyah.mystory.data.remote

import com.erdiansyah.mystory.data.model.LoginRequest
import com.erdiansyah.mystory.data.model.RegistRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {

    @POST("/v1/login")
    suspend fun loginUser(
        @Body body: LoginRequest
    ): Response<LoginResponse>

    @POST("/v1/register")
    suspend fun registUser(
        @Body body: RegistRequest
    ): Response<RegistResponse>
}