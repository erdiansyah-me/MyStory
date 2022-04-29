package com.erdiansyah.mystory.app.di

import android.content.Context
import androidx.room.Room
import com.erdiansyah.mystory.app.Config
import com.erdiansyah.mystory.data.local.StoryDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object LocalModule {
    @Provides
    fun provideStoryDatabase(
        @ApplicationContext applicationContext: Context
    ) : StoryDatabase = Room
        .databaseBuilder(
            applicationContext,
            StoryDatabase::class.java,
            Config.databaseName
        )
        .fallbackToDestructiveMigration()
        .build()
}