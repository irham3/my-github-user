package com.example.mygithubuser.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mygithubuser.data.FavouriteUserRepository
import com.example.mygithubuser.data.UserRepository
import com.example.mygithubuser.data.local.SettingPreferences
import com.example.mygithubuser.di.Injection

class ViewModelFactory private constructor(
    private val favUserRepository: FavouriteUserRepository,
    private val userRepository: UserRepository,
    private val pref:SettingPreferences)
    : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(FavouriteUserViewModel::class.java)
                -> FavouriteUserViewModel(favUserRepository) as T
            modelClass.isAssignableFrom(DetailUserViewModel::class.java)
                -> DetailUserViewModel(favUserRepository, userRepository) as T
            modelClass.isAssignableFrom(MainViewModel::class.java)
                -> MainViewModel(userRepository) as T
            modelClass.isAssignableFrom(SettingViewModel::class.java)
                -> SettingViewModel(pref) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideFavUserRepository(context),
                    Injection.provideUserRepository(),
                    Injection.provideSettingPreference(context))
            }.also { instance = it }
    }
}