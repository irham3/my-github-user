package com.example.mygithubuser.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mygithubuser.data.remote.response.DetailUserResponse
import com.example.mygithubuser.data.remote.response.ItemsItem
import com.example.mygithubuser.data.remote.retrofit.ApiService
import kotlinx.coroutines.*

class UserRepository private constructor(
    private val apiService: ApiService,
) {

    suspend fun findUsers(keyword: String): LiveData<Result<List<ItemsItem>>> {
        val listUser = MutableLiveData<Result<List<ItemsItem>>>()
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            listUser.postValue(Result.Error(throwable.message.toString()))
        }

        listUser.postValue(Result.Loading())

        // Ambil data dari API menggunakan Kotlin Coroutine
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = apiService.getListUsers(keyword)
            if (response.isSuccessful) {
                if (response.body()?.items.isNullOrEmpty()) {
                    listUser.postValue(Result.Error("User not found"))
                } else {
                    listUser.postValue(Result.Success(response.body()?.items))
                }
            } else {

                val errorMessage: String = when(response.code()) {
                    401 -> "Unauthorized"
                    403 -> "API limit exceeded"
                    422 -> "Query parameter is missing"
                    500 -> "Internal server error"
                    else -> response.message()
                }
                listUser.postValue(Result.Error(errorMessage))
            }
        }
        return listUser
    }

    suspend fun getDetailUser(username: String): LiveData<Result<DetailUserResponse>> {
        val detailUser = MutableLiveData<Result<DetailUserResponse>>()
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            detailUser.postValue(Result.Error(throwable.message.toString()))
        }

        detailUser.postValue(Result.Loading())

        // Ambil data Detail User dari API menggunakan Kotlin Coroutine
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = apiService.getDetailUser(username)
            if (response.isSuccessful) {
                detailUser.postValue(Result.Success(response.body()))
            } else {
                val errorMessage: String = when(response.code()) {
                    401 -> "Unauthorized"
                    403 -> "API limit exceeded"
                    422 -> "Query parameter is missing"
                    500 -> "Internal server error"
                    else -> response.message()
                }
                detailUser.postValue(Result.Error(errorMessage))
            }
        }
        return detailUser
    }

    suspend fun getFollowing(username: String): LiveData<Result<List<ItemsItem>>> {
        val listUser = MutableLiveData<Result<List<ItemsItem>>>()
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            listUser.postValue(Result.Error(throwable.message.toString()))
        }

        listUser.postValue(Result.Loading())

        // Ambil data dari API menggunakan Kotlin Coroutine
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = apiService.getFollowing(username)
            if (response.isSuccessful) {
                if (response.body().isNullOrEmpty()) {
                    listUser.postValue(Result.Error(null))
                } else {
                    listUser.postValue(Result.Success(response.body()))
                }
            } else {
                val errorMessage: String = when(response.code()) {
                    401 -> "Unauthorized"
                    403 -> "API limit exceeded"
                    422 -> "Query parameter is missing"
                    500 -> "Internal server error"
                    else -> response.message()
                }
                listUser.postValue(Result.Error(errorMessage))
            }
        }
        return listUser
    }

    suspend fun getFollowers(username: String): LiveData<Result<List<ItemsItem>>> {
        val listUser = MutableLiveData<Result<List<ItemsItem>>>()
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            listUser.postValue(Result.Error(throwable.message.toString()))
        }

        listUser.postValue(Result.Loading())

        // Ambil data dari API menggunakan Kotlin Coroutine
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = apiService.getFollowers(username)
            if (response.isSuccessful) {
                if (response.body().isNullOrEmpty()) {
                    listUser.postValue(Result.Error(null))
                } else {
                    listUser.postValue(Result.Success(response.body()))
                }
            } else {
                val errorMessage: String = when(response.code()) {
                    401 -> "Unauthorized"
                    403 -> "API limit exceeded"
                    422 -> "Query parameter is missing"
                    500 -> "Internal server error"
                    else -> response.message()
                }
                listUser.postValue(Result.Error(errorMessage))
            }
        }
        return listUser
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService)
            }.also { instance = it }
    }
}