package com.dicoding.restaurantreview.ui.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.restaurantreview.ui.FollowerFollowingFragment

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    var username: String = "username"

    override fun createFragment(position: Int): Fragment {
        val fragment = FollowerFollowingFragment()
        fragment.arguments = Bundle().apply {
            putInt(FollowerFollowingFragment.ARG_POSITION, position + 1)
            putString(FollowerFollowingFragment.ARG_USERNAME, username)
        }
        return fragment
    }

    override fun getItemCount(): Int {
        return 2
    }
}

