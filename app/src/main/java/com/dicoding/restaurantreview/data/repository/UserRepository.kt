package com.dicoding.restaurantreview.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.restaurantreview.data.lokal.entity.FavoriteUser
import com.dicoding.restaurantreview.data.lokal.room.UserDao
import com.dicoding.restaurantreview.data.lokal.room.UserRoomDatabase
import com.dicoding.restaurantreview.util.AppExecutor

class UserRepository(application: Application) {
    private val mUserDao: UserDao
    private val appExecutor: AppExecutor = AppExecutor()

    init {
        val db = UserRoomDatabase.getDatabase(application)
        mUserDao = db.userDao()
    }

    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser> {
        return mUserDao.getFavoriteUserByUsername(username)
    }

    fun getAllUsers(): LiveData<List<FavoriteUser>> = mUserDao.getAllUsers()

    fun insert(note: FavoriteUser) {
        appExecutor.diskIO.execute {
            mUserDao.insert(note)
        }
    }

    fun delete(note: FavoriteUser) {
        appExecutor.diskIO.execute {
            mUserDao.delete(note)
        }
    }
}