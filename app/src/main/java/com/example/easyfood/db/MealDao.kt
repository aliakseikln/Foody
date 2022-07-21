package com.example.easyfood.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.easyfood.pojo.Meal

@Dao
interface MealDao {

    //9 видео ошибки с suspend
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(meal: Meal)

    //suspend fun upsert(meal: Meal)
    //9 видео ошибки с suspend//
    @Delete
    suspend fun delete(meal: Meal)

    //suspend fun delete(meal: Meal)
    @Query("SELECT * FROM mealInformation")
    fun getAllMeals(): LiveData<List<Meal>>
}