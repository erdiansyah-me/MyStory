package com.erdiansyah.mystory.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.erdiansyah.mystory.data.remote.ListStoryItem
import com.erdiansyah.mystory.data.remote.RemoteDataSource
import javax.inject.Inject

class StoryPagingSource @Inject constructor(
    private val dataSource: RemoteDataSource
) : PagingSource<Int, ListStoryItem>() {
    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val responseData = dataSource.getStory(page, params.loadSize)

            LoadResult.Page(
                data = responseData.data?.listStory ?: emptyList(),
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (responseData.data?.listStory.isNullOrEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}