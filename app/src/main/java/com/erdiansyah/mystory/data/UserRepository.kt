package com.erdiansyah.mystory.data

import com.erdiansyah.mystory.data.remote.LoginResponse
import com.erdiansyah.mystory.data.remote.LoginResult
import com.erdiansyah.mystory.data.remote.RegistResponse
import com.erdiansyah.mystory.data.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val preference: UserPreference
) {
    suspend fun loginUser(email: String, password: String) : Result<LoginResponse> {
        val result = remoteDataSource.loginUser(email, password)
        if (result is Result.Success) {
            if (result.data?.loginResult != null) {
                preference.saveUser(result.data.loginResult)
            }
        }
        return result
    }
    fun getSession(): Flow<LoginResult?> {
        return preference.getUser()
    }

    suspend fun logout() {
        return preference.logout()
    }

    suspend fun registUser(name: String, email: String, password: String): Result<RegistResponse> {
        return remoteDataSource.registUser(name, email, password)
    }
}