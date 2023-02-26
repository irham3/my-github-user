package com.example.mygithubuser.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mygithubuser.data.local.entity.FavouriteUser

@Database(entities = [FavouriteUser::class], version = 1, exportSchema = false)
abstract class FavouriteUserRoomDatabase : RoomDatabase() {
    abstract fun favUserDao() : FavouriteUserDao

    companion object {
        @Volatile
        private var instance: FavouriteUserRoomDatabase? = null

        fun getInstance(context: Context): FavouriteUserRoomDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    FavouriteUserRoomDatabase::class.java, "GithubUser.db"
                ).build()
            }
        }
    }
}