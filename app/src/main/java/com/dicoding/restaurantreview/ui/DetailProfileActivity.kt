package com.dicoding.restaurantreview.ui

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.dicoding.restaurantreview.R
import com.dicoding.restaurantreview.data.lokal.entity.FavoriteUser
import com.dicoding.restaurantreview.data.remote.response.DetailUserResponse
import com.dicoding.restaurantreview.data.remote.retrofit.ApiConfig
import com.dicoding.restaurantreview.databinding.ActivityDetailProfileBinding
import com.dicoding.restaurantreview.ui.adapter.SectionsPagerAdapter
import com.dicoding.restaurantreview.ui.factory.DarkModelFactory
import com.dicoding.restaurantreview.ui.viewmodel.FollowerFollowingViewModel
import com.dicoding.restaurantreview.ui.viewmodel.MainViewModel
import com.dicoding.restaurantreview.ui.viewmodel.UserAddUpdateViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailProfileBinding
    private lateinit var sectionsPagerAdapter: SectionsPagerAdapter
    private var username: String = "username"
    private var id: Int? = null
    private lateinit var viewModel: FollowerFollowingViewModel
    private lateinit var userViewModel: UserAddUpdateViewModel
    private lateinit var progressBar2: ProgressBar
    private var isFavorite: Boolean = false
    private var avatarUrl: String? = null
    private lateinit var mainViewModel: MainViewModel

    private fun loadDataFromApi() {
        progressBar2.visibility = View.VISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val factory = DarkModelFactory(SettingPreferences.getInstance(application.dataStore))
        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        mainViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        username = intent.getStringExtra("username") ?: ""
        progressBar2 = findViewById(R.id.progressBar2)
        loadDataFromApi()
        getUserDetail(username)
        sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        viewModel = ViewModelProvider(this)[FollowerFollowingViewModel::class.java]
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(
                if (position == 0) R.string.tab_text_1 else R.string.tab_text_2
            )
        }.attach()
        supportActionBar?.elevation = 0f
        userViewModel = ViewModelProvider(this)[UserAddUpdateViewModel::class.java]
        checkIsFavorite()
        userViewModel.getFavoriteUserByUsername(username).observe(this) { favoriteUser ->
            isFavorite = favoriteUser != null
            updateFavoriteButtonIcon()
        }
        binding.addFav.setOnClickListener {
            toggleFavoriteStatus(username, avatarUrl, id)
        }

    }

    private fun addToFavorites(username: String, avatarUrl: String?, id: Int?) {
        val favoriteUser = FavoriteUser(username, avatarUrl, id)
        userViewModel.insert(favoriteUser)
        Snackbar.make(binding.root, "User added to favorites", Snackbar.LENGTH_SHORT).show()
        isFavorite = true
        updateFavoriteButtonIcon()
    }

    private fun removeFromFavorites(username: String, avatarUrl: String?, id: Int?) {
        val favoriteUser = FavoriteUser(username, avatarUrl, id)
        userViewModel.delete(favoriteUser)
        Snackbar.make(binding.root, "User removed from favorites", Snackbar.LENGTH_SHORT).show()
        isFavorite = false
        updateFavoriteButtonIcon()
    }

    private fun toggleFavoriteStatus(username: String, avatarUrl: String?, id: Int?) {
        lifecycleScope.launch {
            if (isFavorite) {
                removeFromFavorites(username, avatarUrl, id)
            } else {
                addToFavorites(username, avatarUrl, id)
            }
        }
    }

    private fun checkIsFavorite() {
        userViewModel.getFavoriteUserByUsername(username).observe(this) { favoriteUser ->
            isFavorite = favoriteUser != null
            updateFavoriteButtonIcon()
        }
    }

    private fun updateFavoriteButtonIcon() {
        if (isFavorite) {
            binding.addFav.setImageResource(R.drawable.ic_favorite)
        } else {
            binding.addFav.setImageResource(R.drawable.ic_favorite_border)
        }
    }

    private fun getUserDetail(username: String) {
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(call: Call<DetailUserResponse>, response: Response<DetailUserResponse>) {
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    userResponse?.let {
                        displayUserDetail(it)
                        avatarUrl = it.avatarUrl
                        id = it.id
                    }
                    progressBar2.visibility = View.GONE
                } else {
                    showErrorSnackbar("Failed to retrieve user data")
                    progressBar2.visibility = View.GONE
                }
            }
            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                showErrorSnackbar("Failed to connect to server")
                progressBar2.visibility = View.GONE
            }
        })
    }

    private fun displayUserDetail(userResponse: DetailUserResponse) {
        Picasso.get().load(userResponse.avatarUrl).into(binding.imageProfile)
        binding.textName.text = userResponse.login ?: ""
        binding.idUser.text = userResponse.id?.toString() ?: ""
        binding.realname.text = userResponse.name?: ""
        binding.textFollowersCount.text = userResponse.followers?.toString() ?: ""
        binding.textFollowingCount.text = userResponse.following?.toString() ?: ""
    }

    private fun showErrorSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
        progressBar2.visibility = View.GONE
    }
}
