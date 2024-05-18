package com.dicoding.restaurantreview.data.remote.retrofit

import com.dicoding.restaurantreview.data.remote.response.DetailUserResponse
import com.dicoding.restaurantreview.data.remote.response.FollowersResponseItem
import com.dicoding.restaurantreview.data.remote.response.FollowingResponseItem
import com.dicoding.restaurantreview.data.remote.response.SearchResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun searchUsers(
        @Header("Authorization") token: String,
        @Query("q") query: String
    ): Call<SearchResponse>

    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String
    ): Call<List<FollowersResponseItem>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String
    ): Call<List<FollowingResponseItem>>
}

