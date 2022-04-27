package com.erdiansyah.mystory.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.erdiansyah.mystory.data.remote.ListStoryItem

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<ListStoryItem>)

    @Query("SELECT * FROM liststoryitem")
    fun getAllStory(): PagingSource<Int, ListStoryItem>

    @Query("DELETE FROM liststoryitem")
    suspend fun deleteAll()
}