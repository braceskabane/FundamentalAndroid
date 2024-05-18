package com.dicoding.restaurantreview.data.lokal.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dicoding.restaurantreview.data.lokal.entity.FavoriteUser

@Database(entities = [FavoriteUser::class], version = 3)
abstract class UserRoomDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: UserRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): UserRoomDatabase {
            if (INSTANCE == null) {
                synchronized(UserRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        UserRoomDatabase::class.java, "user_database"
                    )
                        .addMigrations(MIGRATION_1_3)
                        .build()
                }
            }
            return INSTANCE as UserRoomDatabase
        }

        private val MIGRATION_1_3: Migration = object : Migration(1, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
            }
        }
    }
}
