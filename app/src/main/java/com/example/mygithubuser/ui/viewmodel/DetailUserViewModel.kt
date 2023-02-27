package com.example.mygithubuser.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygithubuser.data.FavouriteUserRepository
import com.example.mygithubuser.data.UserRepository
import com.example.mygithubuser.data.local.entity.FavouriteUser
import kotlinx.coroutines.launch

class DetailUserViewModel(
    private val favUserRepository: FavouriteUserRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    suspend fun findDetailUser(username: String) = userRepository.getDetailUser(username)

    suspend fun findFollowers(username: String) = userRepository.getFollowers(username)

    suspend fun findFollowing(username: String) = userRepository.getFollowing(username)

    fun addToFavourites(favUsers: FavouriteUser) = viewModelScope.launch {
        favUserRepository.addToFavourites(favUsers)
    }

    fun removeFromFavourites(favUsers: FavouriteUser) = viewModelScope.launch {
        favUserRepository.removeFromFavourites(favUsers)
    }

    fun getFavouriteByUsername(username: String): LiveData<List<FavouriteUser>> =
        favUserRepository.getFavoriteUserByUsername(username)

}