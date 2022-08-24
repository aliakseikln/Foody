package com.example.foody.data.db

import androidx.lifecycle.LiveData
import com.example.foody.data.models.MealDataBase

interface RoomRepository {

    val mealList: LiveData<List<MealDataBase>>

    fun insertFavoriteMeal(meal: MealDataBase)

    fun getMealById(mealId: String): MealDataBase

    fun deleteMealById(mealId: String)

    fun deleteMeal(meal: MealDataBase)
}