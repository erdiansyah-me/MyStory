package com.erdiansyah.mystory.app.di

import com.erdiansyah.mystory.BuildConfig
import com.erdiansyah.mystory.app.Config
import com.erdiansyah.mystory.data.AuthInterceptor
import com.erdiansyah.mystory.data.UserPreference
import com.erdiansyah.mystory.data.remote.StoryService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }
    }
    @Provides
    fun provideOkHTTPClient(
        loggingInterceptor: HttpLoggingInterceptor,
        preference: UserPreference
    ): OkHttpClient {
        var token: String?
        runBlocking {
            token = preference.getUser().first()?.token
        }
        return OkHttpClient
            .Builder()
            .addInterceptor(AuthInterceptor(token))
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit
        .Builder()
        .baseUrl(Config.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    @Provides
    fun provideStoryService(
        retrofit: Retrofit
    ) : StoryService {
        return retrofit.create(StoryService::class.java)
    }
}