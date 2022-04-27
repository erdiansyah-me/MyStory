package com.erdiansyah.mystory.data.remote

import retrofit2.Retrofit
import java.lang.Exception
import javax.inject.Inject
import com.erdiansyah.mystory.data.Result
import com.erdiansyah.mystory.data.model.LoginRequest
import com.erdiansyah.mystory.data.model.RegistRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody

class RemoteDataSource @Inject constructor(
    retrofit: Retrofit
) {
    private val retrofitServiceUser = retrofit.create(UserService::class.java)
    private val retrofitServiceStory = retrofit.create(StoryService::class.java)

    suspend fun loginUser(email: String, password: String): Result<LoginResponse>{
        return try {
            val response = retrofitServiceUser.loginUser(LoginRequest(email, password))
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null){
                    Result.Success(body)
                }else{
                    Result.Error("Login Error Coba Lagi")
                }
            } else {
                Result.Error("Kesalahan Login")
            }
        }catch (e: Exception) {
            Result.Error("Terdapat Kesalahan")
        }
    }

    suspend fun registUser(name: String, email: String, password: String): Result<RegistResponse>{
        return try {
            val response = retrofitServiceUser.registUser(RegistRequest(name, email, password))
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null){
                    Result.Success(body)
                } else {
                    Result.Error("Registrasi Error Coba Lagi")
                }
            } else {
                Result.Error("Kesalahan saat Registrasi")
            }
        } catch (e: Exception) {
            Result.Error("Terdapat Kesalahan")
        }
    }

    suspend fun getStory(page: Int?, size: Int?): Result<StoryResponse> {
        return try {
            val response = retrofitServiceStory.getStory(page, size)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.Success(body)
                } else {
                    Result.Error("Kesalahan saat mengambil data Story")
                }
            } else {
                Result.Error("Terdapat Kesalahan saat getResponse")
            }
        } catch (e: Exception) {
                Result.Error("Terdapat Kesalahan")
        }
    }

    suspend fun getStoryLocation(): Result<StoryLocationResponse> {
        return try {
            val response = retrofitServiceStory.getStoryLocation()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.Success(body)
                } else {
                    Result.Error("Kesalahan saat mengambil data Story")
                }
            } else {
                Result.Error("Terdapat Kesalahan saat getResponse")
            }
        } catch (e: Exception) {
            Result.Error("Terdapat Kesalahan")
        }
    }

    suspend fun postStory(image: MultipartBody.Part, description: RequestBody, lat: Double?, lon: Double?) : Result<PostStoryResponse> {
        return try {
            val response = retrofitServiceStory.postStory(image,description, lat, lon)
            if (response.isSuccessful){
                val body = response.body()
                if (body != null) {
                    Result.Success(body)
                } else {
                    Result.Error("Kesalahan pada Upload Story")
                }
            } else {
                Result.Error("Terdapat Kesalahan, Response gagal")
            }
        } catch (e: Exception) {
            Result.Error("Terdapat Kesalahan")
        }
    }
}