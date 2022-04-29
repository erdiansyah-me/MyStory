package com.erdiansyah.mystory.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.erdiansyah.mystory.data.local.StoryDatabase
import com.erdiansyah.mystory.data.remote.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class StoryRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val database: StoryDatabase,
) {
    @OptIn(ExperimentalPagingApi::class)
    fun getStory(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10, enablePlaceholders = false
            ),
            remoteMediator = StoryRemoteMediator(database, remoteDataSource),
            pagingSourceFactory = {
                database.storyDao().getAllStory()
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