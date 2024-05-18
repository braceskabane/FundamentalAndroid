package com.dicoding.restaurantreview.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.restaurantreview.databinding.FragmentFollowerBinding
import com.dicoding.restaurantreview.ui.adapter.ReviewAdapter
import com.dicoding.restaurantreview.ui.viewmodel.FollowerFollowingViewModel
import com.dicoding.restaurantreview.ui.viewmodel.MainViewModel

class FollowerFollowingFragment : Fragment() {

    private lateinit var binding: FragmentFollowerBinding
    private lateinit var adapter: ReviewAdapter
    private lateinit var viewModel: FollowerFollowingViewModel
    private lateinit var progressBar3: ProgressBar
    private lateinit var mainViewModel: MainViewModel

    companion object {
        const val ARG_USERNAME = "username"
        const val ARG_POSITION = "0"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        mainViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        var position = arguments?.getInt(ARG_POSITION, 0) ?: 0
        var username = arguments?.getString(ARG_USERNAME) ?: ""
        progressBar3 = binding.progressBar3
        viewModel = ViewModelProvider(this)[FollowerFollowingViewModel::class.java]
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            progressBar3.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        viewModel = ViewModelProvider(this)[FollowerFollowingViewModel::class.java]
        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME) ?: ""
        }
        arguments?.let {
            adapter = ReviewAdapter()
            binding.userRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.userRecyclerView.adapter = adapter
        }
        if (position == 1){
            viewModel.getFollowers(username)
            viewModel.followers.observe(viewLifecycleOwner) { followersList ->
                adapter.submitList(followersList)
            }
        } else {
            viewModel.getFollowing(username)
            viewModel.following.observe(viewLifecycleOwner) { followingList ->
                adapter.submitList(followingList)
            }
        }
    }
}
