package com.example.mygithubuser.data.remote.retrofit

import com.example.mygithubuser.data.remote.response.DetailUserResponse
import com.example.mygithubuser.data.remote.response.GithubResponse
import com.example.mygithubuser.data.remote.response.ItemsItem
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    suspend fun getListUsers(@Query("q") q: String): Response<GithubResponse>

    @GET("users/{username}")
    suspend fun getDetailUser(@Path("username") username: String): Response<DetailUserResponse>

    @GET("users/{username}/followers")
    suspend fun getFollowers(@Path("username") username: String): Response<List<ItemsItem>>

    @GET("users/{username}/following")
    suspend fun getFollowing(@Path("username") username: String): Response<List<ItemsItem>>
}