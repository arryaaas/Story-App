package com.arya.storyapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.arya.storyapp.data.local.entity.RemoteKeys
import com.arya.storyapp.data.local.entity.StoryEntity

@Database(
    entities = [StoryEntity::class, RemoteKeys::class],
    version = 2,
    exportSchema = false
)
abstract class StoryDatabase : RoomDatabase() {

    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao

//    companion object {
//        @Volatile
//        private var INSTANCE: StoryDatabase? = null
//
//        @JvmStatic
//        fun getDatabase(context: Context): StoryDatabase {
//            return INSTANCE ?: synchronized(this) {
//                INSTANCE ?: Room.databaseBuilder(
//                    context.applicationContext,
//                    StoryDatabase::class.java, "story_database"
//                )
//                    .fallbackToDestructiveMigration()
//                    .build()
//                    .also { INSTANCE = it }
//            }
//        }
//    }
}