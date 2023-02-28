package com.example.mygithubuser.data

import androidx.lifecycle.LiveData
import com.example.mygithubuser.data.local.entity.FavouriteUser
import com.example.mygithubuser.data.local.room.FavouriteUserDao

class FavouriteUserRepository private constructor(
    private val mFavUsersDao: FavouriteUserDao
)  {

    fun getAllFavourites(): LiveData<List<FavouriteUser>> = mFavUsersDao.getAllFavourites()

    fun getFavoriteUserByUsername(username: String): LiveData<FavouriteUser?>
        = mFavUsersDao.getFavoriteUserByUsername(username)

    suspend fun addToFavourites(favouriteUser: FavouriteUser) {
        mFavUsersDao.insert(favouriteUser)
    }

    suspend fun removeFromFavourites(favouriteUser: FavouriteUser) {
        mFavUsersDao.delete(favouriteUser)
    }

    companion object {
        @Volatile
        private var instance: FavouriteUserRepository? = null
        fun getInstance(
            mFavUsersDao: FavouriteUserDao
        ): FavouriteUserRepository =
            instance ?: synchronized(this) {
                instance ?: FavouriteUserRepository(mFavUsersDao)
            }.also { instance = it }
    }
}