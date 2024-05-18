package com.dicoding.restaurantreview.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.restaurantreview.BuildConfig
import com.dicoding.restaurantreview.data.remote.response.ItemsItem
import com.dicoding.restaurantreview.data.remote.response.SearchResponse
import com.dicoding.restaurantreview.data.remote.retrofit.ApiConfig
import com.dicoding.restaurantreview.databinding.ActivityMainBinding
import com.dicoding.restaurantreview.ui.adapter.ReviewAdapter
import com.dicoding.restaurantreview.ui.factory.DarkModelFactory
import com.dicoding.restaurantreview.ui.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ReviewAdapter
    private var dataList: List<ItemsItem> = listOf()
    private lateinit var progressBar: ProgressBar
    private lateinit var mySuperSecretKey: String
    private var query: String = "android"
    private lateinit var mainViewModel: MainViewModel

    companion object {
        private const val TAG = "MainActivity"
    }
    private fun loadDataFromApi() {
        progressBar.visibility = View.VISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
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
        progressBar = binding.progressBar
        mySuperSecretKey = BuildConfig.KEY
        adapter = ReviewAdapter()
        binding.userRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.userRecyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        binding.userRecyclerView.adapter = adapter
        loadDataFromApi()
        fetchData()
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        val query = s?.toString()?.trim() ?: ""
                        if (query.isNotEmpty()) {
                            searchUser(query)
                            clearData()
                        } else{
                            fetchData()
                        }
                    }
                    override fun afterTextChanged(s: Editable?) {
                    }
                })
        }
        binding.buttonFav.setOnClickListener {
            startActivity(Intent(this, FavUserActivity::class.java))
        }
        binding.buttonMode.setOnClickListener {
            startActivity(Intent(this, DarkModeActivity::class.java))
        }
    }

    private fun clearData() {
        adapter.submitList(emptyList())
    }

    private fun fetchData() {
        val client = ApiConfig.getApiService().searchUsers(mySuperSecretKey, query)
        client.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.items != null) {
                        dataList = responseBody.items.filterNotNull()
                        adapter.submitList(dataList)
                        progressBar.visibility = View.GONE
                    } else {
                        Log.e(TAG, "Response body or items is null")
                        showErrorSnackbar("Failed to retrieve data")
                        progressBar.visibility = View.GONE
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    showErrorSnackbar("Failed to retrieve data")
                    progressBar.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
                showErrorSnackbar("Failed to connect to server")
                progressBar.visibility = View.GONE
            }
        })
    }

    fun searchUser (username: String) {
        val client = ApiConfig.getApiService().searchUsers(mySuperSecretKey, username)
        client.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.items != null) {
                        dataList = responseBody.items.filterNotNull()
                        if (username == binding.searchView.text.toString()) {
                            adapter.submitList(dataList)
                            progressBar.visibility = View.GONE
                        } else {
                            adapter.submitList(emptyList())
                            progressBar.visibility = View.GONE
                        }
                    } else {
                        Log.e(TAG, "Response body or items is null")
                        showErrorSnackbar("Failed to retrieve data")
                        progressBar.visibility = View.GONE
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    showErrorSnackbar("Failed to retrieve data")
                    progressBar.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
                showErrorSnackbar("Failed to connect to server")
                progressBar.visibility = View.GONE
            }
        })
    }

    private fun showErrorSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
        progressBar.visibility = View.GONE
    }
}