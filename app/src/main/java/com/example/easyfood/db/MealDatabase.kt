package com.example.easyfood.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.easyfood.pojo.Meal


@Database(entities = [Meal::class], version = 1)
@TypeConverters(MealTypeConverter::class)
abstract class MealDatabase : RoomDatabase() {
    abstract fun mealDao(): MealDao

    companion object {

        @Volatile
       private var database: MealDatabase? = null

        @Synchronized
        fun getInstance(context: Context): MealDatabase {
           return if (database == null) {
                database = Room.databaseBuilder(context, MealDatabase::class.java, "meal.db")
                    .fallbackToDestructiveMigration()
                    .build()
               database as MealDatabase
            } else {
               database as MealDatabase
           }

        }
    }
}