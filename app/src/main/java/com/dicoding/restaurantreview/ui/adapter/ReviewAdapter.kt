package com.dicoding.restaurantreview.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.restaurantreview.data.lokal.entity.FavoriteUser
import com.dicoding.restaurantreview.data.remote.response.FollowersResponseItem
import com.dicoding.restaurantreview.data.remote.response.FollowingResponseItem
import com.dicoding.restaurantreview.data.remote.response.ItemsItem
import com.dicoding.restaurantreview.databinding.ItemReviewBinding
import com.dicoding.restaurantreview.ui.DetailProfileActivity
import com.squareup.picasso.Picasso


class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.MyViewHolder>() {

    private var items: List<Any> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemReviewBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        when (val item = items[position]) {
            is ItemsItem -> holder.bind(item)
            is FollowersResponseItem -> holder.bindFollower(item)
            is FollowingResponseItem -> holder.bindFollowing(item)
            is FavoriteUser -> holder.bindFavorite(item)
            else -> throw IllegalArgumentException("Unsupported item type")
        }
    }

    override fun getItemCount(): Int = items.size

    fun submitList(newItems: List<Any>) {
        val diffResult = DiffUtil.calculateDiff(ReviewDiffCallback(items, newItems))
        this.items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    private class ReviewDiffCallback(
        private val oldList: List<Any>,
        private val newList: List<Any>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return when {
                oldItem is ItemsItem && newItem is ItemsItem -> oldItem.id == newItem.id
                oldItem is FollowersResponseItem && newItem is FollowersResponseItem -> oldItem.id == newItem.id
                oldItem is FollowingResponseItem && newItem is FollowingResponseItem -> oldItem.id == newItem.id
                oldItem is FavoriteUser && newItem is FavoriteUser -> oldItem.id == newItem.id
                else -> false
            }
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem == newItem
        }
    }

    inner class MyViewHolder(private val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemsItem) {
            Picasso.get().load(item.avatarUrl).into(binding.photoProfile)
            binding.textName.text = item.login
            binding.idTextView.text = item.id.toString()
            binding.root.setOnClickListener {
                val intentDetail = Intent(binding.root.context, DetailProfileActivity::class.java)
                intentDetail.putExtra("username", item.login)
                binding.root.context.startActivity(intentDetail)
            }
        }
        fun bindFavorite(item: FavoriteUser){
            Picasso.get().load(item.avatarUrl).into(binding.photoProfile)
            binding.textName.text = item.username
            binding.idTextView.text = item.id?.toString() ?:""
            binding.root.setOnClickListener {
                val intentDetail = Intent(binding.root.context, DetailProfileActivity::class.java)
                intentDetail.putExtra("username", item.username)
                binding.root.context.startActivity(intentDetail)
            }
        }
        fun bindFollower(item: FollowersResponseItem) {
            Picasso.get().load(item.avatarUrl).into(binding.photoProfile)
            binding.textName.text = item.login
            binding.idTextView.text = item.id.toString()
        }
        fun bindFollowing(item: FollowingResponseItem) {
            Picasso.get().load(item.avatarUrl).into(binding.photoProfile)
            binding.textName.text = item.login
            binding.idTextView.text = item.id.toString()
        }
    }
}
