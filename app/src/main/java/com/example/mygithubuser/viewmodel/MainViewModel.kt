package com.example.mygithubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mygithubuser.api.ApiConfig
import com.example.mygithubuser.Event
import com.example.mygithubuser.response.GithubResponse
import com.example.mygithubuser.response.ItemsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel: ViewModel() {

    private val _listUser = MutableLiveData<List<ItemsItem>>()
    val listUser: LiveData<List<ItemsItem>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _toastMessage = MutableLiveData<Event<String>>()
    val toastMessage: LiveData<Event<String>> = _toastMessage

    init{
        findUsers("irham3")
    }

    fun findUsers(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getListUsers(username)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    if (response.body()?.totalCount == 0)
                        _toastMessage.value = Event("Username Github tidak ditemukan")
                    else
                        _listUser.value = response.body()?.items!!
                } else {
                    _toastMessage.value = Event("onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                _toastMessage.value = Event("onFailure: ${t.message.toString()}")
            }
        })
    }
}