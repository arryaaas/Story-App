package com.arya.storyapp.di

import android.content.Context
import androidx.room.Room
import com.arya.storyapp.data.local.SessionDataStore
import com.arya.storyapp.data.local.database.RemoteKeysDao
import com.arya.storyapp.data.local.database.StoryDao
import com.arya.storyapp.data.local.database.StoryDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): SessionDataStore = SessionDataStore(context)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): StoryDatabase = Room.databaseBuilder(
        context,
        StoryDatabase::class.java, "Story.db"
    ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideStoryDao(database: StoryDatabase): StoryDao = database.storyDao()

    @Provides
    fun provideRemoteKeysDao(database: StoryDatabase): RemoteKeysDao = database.remoteKeysDao()
}