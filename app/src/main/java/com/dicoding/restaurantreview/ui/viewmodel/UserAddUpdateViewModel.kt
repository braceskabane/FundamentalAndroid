package com.dicoding.restaurantreview.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.restaurantreview.data.lokal.entity.FavoriteUser
import com.dicoding.restaurantreview.data.repository.UserRepository
import kotlinx.coroutines.launch

class UserAddUpdateViewModel @JvmOverloads constructor(application: Application) : AndroidViewModel(application) {

    private val mUserRepository: UserRepository = UserRepository(application)

    fun insert(user: FavoriteUser) {
        viewModelScope.launch {
            mUserRepository.insert(user)
        }
    }
    fun delete(user: FavoriteUser) {
        viewModelScope.launch {
            mUserRepository.delete(user)
        }
    }

    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser> {
        return mUserRepository.getFavoriteUserByUsername(username)
    }
}