package com.example.easyfood.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.example.easyfood.data.models.MealDataBase

@Dao
interface Dao {

    @Insert
    fun insertFavorite(meal: MealDataBase)

    @Update
    fun updateFavorite(meal:MealDataBase)

    @Query("SELECT * FROM meal_information order by mealId asc")
    fun getAllMeals():LiveData<List<MealDataBase>>

    @Query("SELECT * FROM meal_information WHERE mealId =:id")
    fun getMealById(id:String):MealDataBase

    @Query("DELETE FROM meal_information WHERE mealId =:id")
    fun deleteMealById(id:String)

    @Delete
    fun deleteMeal(meal:MealDataBase)
}