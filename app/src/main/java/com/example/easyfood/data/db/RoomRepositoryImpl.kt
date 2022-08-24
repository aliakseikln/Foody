package com.example.easyfood.data.db

import androidx.lifecycle.LiveData
import com.example.easyfood.data.models.MealDataBase

class RoomRepositoryImpl(private val mealDao: Dao) : RoomRepository {

    override val mealList: LiveData<List<MealDataBase>> = mealDao.getAllMeals()

    override fun insertFavoriteMeal(meal: MealDataBase) {
        mealDao.insertFavorite(meal)
    }

    override fun getMealById(mealId: String): MealDataBase {
        return mealDao.getMealById(mealId)
    }

    override fun deleteMealById(mealId: String) {
        mealDao.deleteMealById(mealId)
    }

    override fun deleteMeal(meal: MealDataBase) = mealDao.deleteMeal(meal)
}