package com.example.mygithubuser.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygithubuser.data.UserRepository
import kotlinx.coroutines.launch

class MainViewModel (private val userRepository: UserRepository): ViewModel() {

    init {
        // Secara default, user yang tampil dari query ini
        viewModelScope.launch { findUsers("irham3") }
    }

    suspend fun findUsers(keyword: String) = userRepository.findUsers(keyword)
}