package com.example.mygithubuser.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mygithubuser.data.local.entity.FavouriteUser

interface FavouriteUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(note: FavouriteUser)

    @Update
    fun update(note: FavouriteUser)

    @Delete
    fun delete(note: FavouriteUser)

    @Query("SELECT * from favourite_user ORDER BY id ASC")
    fun getAllFavourites(): LiveData<List<FavouriteUser>>
}