package com.example.mygithubuser.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.mygithubuser.data.local.entity.FavouriteUser
import com.example.mygithubuser.data.local.room.FavouriteUserDao
import com.example.mygithubuser.data.local.room.FavouriteUserRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavouriteUserRepository(application: Application) {
    private val mFavUsersDao: FavouriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavouriteUserRoomDatabase.getDatabase(application)
        mFavUsersDao = db.favUserDao()
    }

    fun getAllFavourites(): LiveData<List<FavouriteUser>> = mFavUsersDao.getAllFavourites()

    fun insert(favUser: FavouriteUser) {
        executorService.execute { mFavUsersDao.insert(favUser) }
    }

    fun delete(favUser: FavouriteUser) {
        executorService.execute { mFavUsersDao.delete(favUser) }
    }

    fun update(favUser: FavouriteUser) {
        executorService.execute { mFavUsersDao.update(favUser) }
    }
}