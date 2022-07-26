package com.example.easyfood.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.easyfood.data.db.MealDatabase
import com.example.easyfood.data.db.Repository
import com.example.easyfood.data.pojo.MealDB
import com.example.easyfood.data.pojo.MealDetail
import com.example.easyfood.data.pojo.RandomMealResponse
import com.example.easyfood.data.retrofit.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsViewModel(application: Application) : AndroidViewModel(application) {
    private val mutableMealDetail = MutableLiveData<List<MealDetail>>()
    private val mutableMealBottomSheet = MutableLiveData<List<MealDetail>>()
    private var allMeals: LiveData<List<MealDB>>
    private var repository: Repository

    init {
        val mealDao = MealDatabase.getInstance(application).dao()
        repository = Repository(mealDao)
        allMeals = repository.mealList
    }

    fun insertMeal(meal: MealDB) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertFavoriteMeal(meal)
            withContext(Dispatchers.Main) {
            }
        }


    fun deleteMeal(meal: MealDB) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteMeal(meal)
        }

    fun getMealById(id: String) {
        RetrofitInstance.foodApi.getMealById(id).enqueue(object : Callback<RandomMealResponse> {
            override fun onResponse(
                call: Call<RandomMealResponse>,
                response: Response<RandomMealResponse>,
            ) {
                mutableMealDetail.value = response.body()!!.meals
            }

            override fun onFailure(call: Call<RandomMealResponse>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }
        })
    }

    fun isMealSavedInDatabase(mealId: String): Boolean {
        var meal: MealDB?
        runBlocking(Dispatchers.IO) {
            meal = repository.getMealById(mealId)
        }
        if (meal == null)
            return false
        return true
    }

    fun deleteMealById(mealId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteMealById(mealId)
        }
    }

    fun getMealByIdBottomSheet(id: String) {
        RetrofitInstance.foodApi.getMealById(id).enqueue(object : Callback<RandomMealResponse> {
            override fun onResponse(
                call: Call<RandomMealResponse>,
                response: Response<RandomMealResponse>,
            ) {
                mutableMealBottomSheet.value = response.body()!!.meals
            }

            override fun onFailure(call: Call<RandomMealResponse>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }
        })
    }

    fun observeMealDetail(): LiveData<List<MealDetail>> {
        return mutableMealDetail
    }

    fun observeMealBottomSheet(): LiveData<List<MealDetail>> {
        return mutableMealBottomSheet
    }

    fun observeSaveMeal(): LiveData<List<MealDB>> {
        return allMeals
    }
}