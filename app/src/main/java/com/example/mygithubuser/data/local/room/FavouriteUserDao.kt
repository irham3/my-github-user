package com.example.mygithubuser.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mygithubuser.data.local.entity.FavouriteUser
@Dao
interface FavouriteUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favUser: FavouriteUser)

    @Delete
    suspend fun delete(favUser: FavouriteUser)

    @Query("SELECT * FROM FavouriteUser WHERE username = :username")
    fun getFavoriteUserByUsername(username: String): LiveData<FavouriteUser?>

    @Query("SELECT * from FavouriteUser")
    fun getAllFavourites(): LiveData<List<FavouriteUser>>
}