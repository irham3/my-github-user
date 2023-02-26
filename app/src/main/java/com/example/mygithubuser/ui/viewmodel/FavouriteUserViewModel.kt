package com.example.mygithubuser.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mygithubuser.data.FavouriteUserRepository

class FavouriteUserViewModel (private val favUserRepository: FavouriteUserRepository) : ViewModel() {
    fun getAllFavourites() = favUserRepository.getAllFavourites()
}