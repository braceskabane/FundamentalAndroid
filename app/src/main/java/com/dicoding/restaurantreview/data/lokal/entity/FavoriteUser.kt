package com.dicoding.restaurantreview.data.lokal.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class FavoriteUser(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "username")
    var username: String = "",

    @ColumnInfo(name = "avatar")
    var avatarUrl: String? = null,

    @ColumnInfo(name = "id")
    val id: Int? = null,
): Parcelable