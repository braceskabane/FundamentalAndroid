package com.dicoding.restaurantreview.ui.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.restaurantreview.data.repository.UserRepository
import com.dicoding.restaurantreview.ui.viewmodel.FavUserViewModel

class FavoriteUserViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavUserViewModel::class.java)) {
            val repository = UserRepository(application)
            @Suppress("UNCHECKED_CAST")
            return FavUserViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
