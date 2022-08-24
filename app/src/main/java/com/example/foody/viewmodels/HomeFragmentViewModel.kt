package com.example.foody.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foody.data.models.response.CategoryResponse
import com.example.foody.data.models.response.MealRandomResponse
import com.example.foody.data.models.response.MealResponse
import com.example.foody.data.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragmentViewModel : ViewModel() {
    private val mutableCategory = MutableLiveData<CategoryResponse>()
    private val mutableRandomMeal = MutableLiveData<MealRandomResponse>()
    private val mutableMealsByCategory = MutableLiveData<MealResponse>()
    private val listOfMeals = arrayListOf("pork", "beef", "chicken")

    init {
        getRandomMeal()
        getAllCategories()
        getMealsByCategory(listOfMeals.random())
    }

    private fun getAllCategories() {
        RetrofitInstance.foodApi.getCategories().enqueue(object : Callback<CategoryResponse> {
            override fun onResponse(
                call: Call<CategoryResponse>,
                response: Response<CategoryResponse>,
            ) {
                mutableCategory.value = response.body()
            }

            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                Log.d(TAG, t.message.toString())
            }
        })
    }

    private fun getRandomMeal() {
        RetrofitInstance.foodApi.getRandomMeal().enqueue(object : Callback<MealRandomResponse> {
            override fun onResponse(
                call: Call<MealRandomResponse>,
                response: Response<MealRandomResponse>,
            ) {
                mutableRandomMeal.value = response.body()
            }

            override fun onFailure(call: Call<MealRandomResponse>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }
        })
    }

    private fun getMealsByCategory(category: String) {
        RetrofitInstance.foodApi.getMealsByCategory(category)
            .enqueue(object : Callback<MealResponse> {
                override fun onResponse(
                    call: Call<MealResponse>,
                    response: Response<MealResponse>,
                ) {

                    val mealResponse = response.body()
                    if (mealResponse != null) {
                        mealResponse.meals[0].category = category
                    }
                    mutableMealsByCategory.value = mealResponse
                }

                override fun onFailure(call: Call<MealResponse>, t: Throwable) {
                    Log.e(TAG, t.message.toString())
                }
            })
    }

    fun observeMealByCategory(): LiveData<MealResponse> {
        return mutableMealsByCategory
    }

    fun observeRandomMeal(): LiveData<MealRandomResponse> {
        return mutableRandomMeal
    }

    fun observeCategories(): LiveData<CategoryResponse> {
        return mutableCategory
    }

    companion object {
        const val TAG = "MainActivity"
    }
}