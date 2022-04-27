package com.erdiansyah.mystory.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.erdiansyah.mystory.data.remote.ListStoryItem

@Database(
    entities = [ListStoryItem::class, RemoteKeys::class],
    version = 2,
    exportSchema = false
)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}