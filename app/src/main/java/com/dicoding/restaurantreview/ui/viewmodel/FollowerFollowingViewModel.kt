package com.dicoding.restaurantreview.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.restaurantreview.data.remote.response.FollowersResponseItem
import com.dicoding.restaurantreview.data.remote.response.FollowingResponseItem
import com.dicoding.restaurantreview.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowerFollowingViewModel : ViewModel() {
    private val _followers = MutableLiveData<List<FollowersResponseItem>>()
    val followers: LiveData<List<FollowersResponseItem>> = _followers

    private val _following = MutableLiveData<List<FollowingResponseItem>>()
    val following: LiveData<List<FollowingResponseItem>> = _following

    private val _errorMessage = MutableLiveData<String>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private fun setLoadingStatus(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    companion object {
        private const val TAG = "FollowerFollowingViewModelActivity"
    }

    fun getFollowers(username: String) {
        setLoadingStatus(true)
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<FollowersResponseItem>> {
            override fun onResponse(call: Call<List<FollowersResponseItem>>,
                                    response: Response<List<FollowersResponseItem>>
            ) {
                if (response.isSuccessful) {
                    val followersResponse = response.body()
                    _followers.value = followersResponse ?: emptyList()
                    setLoadingStatus(false)
                } else {
                    _errorMessage.value = "Failed to retrieve data"
                    Log.e(TAG, "Failed to retrieve data: ${response.code()}")
                    setLoadingStatus(false)
                }
            }
            override fun onFailure(call: Call<List<FollowersResponseItem>>, t: Throwable) {
                _errorMessage.value = "Failed to retrieve data"
                setLoadingStatus(false)
            }
        })
    }

    fun getFollowing(username: String) {
        setLoadingStatus(true)
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<FollowingResponseItem>> {
            override fun onResponse(
                call: Call<List<FollowingResponseItem>>,
                response: Response<List<FollowingResponseItem>>
            ) {
                if (response.isSuccessful) {
                    val followingResponse = response.body()
                    _following.value = followingResponse ?: emptyList()
                    setLoadingStatus(false)
                } else {
                    _errorMessage.value = "Failed to retrieve data"
                    Log.e(TAG, "Failed to retrieve data: ${response.code()}")
                    setLoadingStatus(false)
                }
            }
            override fun onFailure(call: Call<List<FollowingResponseItem>>, t: Throwable) {
                _errorMessage.value = "Failed to retrieve data"
                setLoadingStatus(false)
            }
        })
    }
}
