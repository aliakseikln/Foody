package com.example.easyfood.data.db

import androidx.lifecycle.LiveData
import com.example.easyfood.data.models.MealDataBase

interface RoomRepository {

    val mealList: LiveData<List<MealDataBase>>

    fun insertFavoriteMeal(meal: MealDataBase)

    fun getMealById(mealId: String): MealDataBase

    fun deleteMealById(mealId: String)

    fun deleteMeal(meal: MealDataBase)
}