package com.dicoding.restaurantreview.data.remote.response

import com.google.gson.annotations.SerializedName

data class SearchResponse(
	@field:SerializedName("items")
	val items: List<ItemsItem?>? = null
)

data class ItemsItem(
	@field:SerializedName("login")
	val login: String? = null,

	@field:SerializedName("avatar_url")
	val avatarUrl: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,
)
