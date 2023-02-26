package com.example.mygithubuser.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.mygithubuser.data.FavouriteUserRepository
import com.example.mygithubuser.data.local.SettingPreferences
import com.example.mygithubuser.data.local.room.FavouriteUserRoomDatabase

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
object Injection {
    fun provideRepository(context: Context): FavouriteUserRepository {
        val database = FavouriteUserRoomDatabase.getInstance(context)
        val dao = database.favUserDao()
        return FavouriteUserRepository.getInstance(dao)
    }

    fun provideSettingPreference(context: Context): SettingPreferences {
        return SettingPreferences.getInstance(context.dataStore)
    }


}