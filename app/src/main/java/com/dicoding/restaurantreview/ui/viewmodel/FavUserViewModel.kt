package com.dicoding.restaurantreview.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.restaurantreview.data.lokal.entity.FavoriteUser
import com.dicoding.restaurantreview.data.repository.UserRepository

class FavUserViewModel(private val repository: UserRepository) : ViewModel() {
    private val _users = MutableLiveData<List<FavoriteUser>>()
    val users: LiveData<List<FavoriteUser>> = _users

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getAllUsers() {
        _isLoading.value = true
        repository.getAllUsers().observeForever { userList ->
            _users.value = userList
            _isLoading.value = false
        }
    }
}