package com.example.easyfood.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.easyfood.data.db.MealDatabase
import com.example.easyfood.data.db.RoomRepository
import com.example.easyfood.data.db.RoomRepositoryImpl
import com.example.easyfood.data.models.MealDataBase
import com.example.easyfood.data.models.MealDetails
import com.example.easyfood.data.models.response.MealRandomResponse
import com.example.easyfood.data.retrofit.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val mutableMealDetails = MutableLiveData<List<MealDetails>>()
    private val mutableMealBottomSheet = MutableLiveData<List<MealDetails>>()
    private var allMeals: LiveData<List<MealDataBase>>
    private var roomRepositoryImpl: RoomRepository

    init {
        val mealDao = MealDatabase.getInstance(application).dao()
        roomRepositoryImpl = RoomRepositoryImpl(mealDao)
        allMeals = roomRepositoryImpl.mealList
    }

    fun insertMeal(meal: MealDataBase) =
        viewModelScope.launch(Dispatchers.IO) {
            roomRepositoryImpl.insertFavoriteMeal(meal)
            withContext(Dispatchers.Main) {
            }
        }


    fun deleteMeal(meal: MealDataBase) =
        viewModelScope.launch(Dispatchers.IO) {
            roomRepositoryImpl.deleteMeal(meal)
        }

    fun getMealById(id: String) {
        RetrofitInstance.foodApi.getMealById(id).enqueue(object : Callback<MealRandomResponse> {
            override fun onResponse(
                call: Call<MealRandomResponse>,
                response: Response<MealRandomResponse>,
            ) {
                mutableMealDetails.value = response.body()!!.meals
            }

            override fun onFailure(call: Call<MealRandomResponse>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }
        })
    }

    fun isMealSavedInDatabase(mealId: String): Boolean {
        var meal: MealDataBase?
        runBlocking(Dispatchers.IO) {
            meal = roomRepositoryImpl.getMealById(mealId)
        }
        if (meal == null)
            return false
        return true
    }

    fun deleteMealById(mealId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            roomRepositoryImpl.deleteMealById(mealId)
        }
    }

    fun getMealByIdBottomSheet(id: String) {
        RetrofitInstance.foodApi.getMealById(id).enqueue(object : Callback<MealRandomResponse> {
            override fun onResponse(
                call: Call<MealRandomResponse>,
                response: Response<MealRandomResponse>,
            ) {
                mutableMealBottomSheet.value = response.body()!!.meals
            }

            override fun onFailure(call: Call<MealRandomResponse>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }
        })
    }

    fun observeMealDetail(): LiveData<List<MealDetails>> {
        return mutableMealDetails
    }

    fun observeMealBottomSheet(): LiveData<List<MealDetails>> {
        return mutableMealBottomSheet
    }

    fun observeSaveMeal(): LiveData<List<MealDataBase>> {
        return allMeals
    }

    companion object {
        const val TAG = "DetailsActivityViewModel"
    }
}