package com.erdiansyah.mystory.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.erdiansyah.mystory.data.local.StoryDao
import com.erdiansyah.mystory.data.local.StoryDatabase
import com.erdiansyah.mystory.data.remote.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.internal.wait
import javax.inject.Inject

class StoryRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val storyDao: StoryDao,
    private val mediator: StoryRemoteMediator
) {
    @OptIn(ExperimentalPagingApi::class)
    fun getStory(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10, enablePlaceholders = false
            ),
            remoteMediator = mediator,
            pagingSourceFactory = {
                storyDao.getAllStory()
            }
        ).liveData
    }

    suspend fun postStory(image: MultipartBody.Part, description: RequestBody, lat: Double?, lon: Double?) : Result<PostStoryResponse> {
        return remoteDataSource.postStory(image, description, lat, lon)
    }
    suspend fun getStoryLocation(): Result<StoryLocationResponse>{
        return remoteDataSource.getStoryLocation()
    }
}