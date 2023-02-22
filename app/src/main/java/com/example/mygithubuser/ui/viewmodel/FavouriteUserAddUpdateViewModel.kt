package com.example.mygithubuser.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.mygithubuser.data.local.entity.FavouriteUser
import com.example.mygithubuser.data.FavouriteUserRepository

class FavouriteUserAddUpdateViewModel(application: Application) : ViewModel() {
    private val mFavUserRepository: FavouriteUserRepository = FavouriteUserRepository (application)

    fun insert(favUser: FavouriteUser) {
        mFavUserRepository.insert(favUser)
    }

    fun update(favUser: FavouriteUser) {
        mFavUserRepository.update(favUser)
    }

    fun delete(favUser: FavouriteUser) {
        mFavUserRepository.delete(favUser)
    }
}