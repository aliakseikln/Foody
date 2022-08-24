package com.example.foody.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.foody.data.models.MealDataBase

@Database(entities = [MealDataBase::class], version = 6)
abstract class MealDatabase : RoomDatabase() {
    abstract fun dao(): Dao

    companion object {
        @Volatile
        private var INSTANCE: MealDatabase? = null

        @Synchronized
        fun getInstance(context: Context): MealDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MealDatabase::class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}

