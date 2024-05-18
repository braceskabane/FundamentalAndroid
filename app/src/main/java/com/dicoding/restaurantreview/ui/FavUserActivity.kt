package com.dicoding.restaurantreview.ui

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.restaurantreview.data.remote.response.ItemsItem
import com.dicoding.restaurantreview.databinding.ActivityFavUserBinding
import com.dicoding.restaurantreview.ui.adapter.ReviewAdapter
import com.dicoding.restaurantreview.ui.factory.DarkModelFactory
import com.dicoding.restaurantreview.ui.factory.FavoriteUserViewModelFactory
import com.dicoding.restaurantreview.ui.viewmodel.FavUserViewModel
import com.dicoding.restaurantreview.ui.viewmodel.MainViewModel

class FavUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavUserBinding
    private lateinit var progressBar5: ProgressBar
    private lateinit var favoriteUserViewModel: FavUserViewModel
    private lateinit var adapter: ReviewAdapter
    private lateinit var mainViewModel: MainViewModel

    private fun loadDataFromApi() {
        progressBar5.visibility = View.VISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val fuki = DarkModelFactory(SettingPreferences.getInstance(application.dataStore))
        mainViewModel = ViewModelProvider(this, fuki)[MainViewModel::class.java]
        mainViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        progressBar5 = binding.progressBar5
        loadDataFromApi()

        adapter = ReviewAdapter()
        binding.userRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.userRecyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        binding.userRecyclerView.adapter = adapter

        val factory = FavoriteUserViewModelFactory(application)
        favoriteUserViewModel = ViewModelProvider(this, factory)[FavUserViewModel::class.java]

        favoriteUserViewModel.isLoading.observe(this) { isLoading ->
            progressBar5.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        favoriteUserViewModel.users.observe(this) { users ->
            val items = arrayListOf<ItemsItem>()
            users.map {
                val item = ItemsItem(login = it.username, avatarUrl = it.avatarUrl, id = it.id)
                items.add(item)
            }
            adapter.submitList(items)
        }
        favoriteUserViewModel.getAllUsers()
    }
}