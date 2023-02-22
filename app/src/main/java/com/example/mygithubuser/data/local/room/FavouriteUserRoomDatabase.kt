package com.example.mygithubuser.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mygithubuser.data.local.entity.FavouriteUser

@Database(entities = [FavouriteUser::class], version = 1)
abstract class FavouriteUserRoomDatabase : RoomDatabase() {
    abstract fun favUserDao() : FavouriteUserDao

    companion object {
        @Volatile
        private var INSTANCE: FavouriteUserRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): FavouriteUserRoomDatabase {
            if (INSTANCE == null) {
               synchronized(FavouriteUserRoomDatabase::class.java) {
                   INSTANCE = Room.databaseBuilder(context.applicationContext,
                            FavouriteUserRoomDatabase::class.java, "favourite_user_database")
                            .build()
               }
            }

            return INSTANCE as FavouriteUserRoomDatabase
        }
    }
}