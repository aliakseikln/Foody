package com.example.easyfood.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.easyfood.data.models.Meal
import com.example.easyfood.data.models.MealDetails
import com.example.easyfood.data.models.response.MealDetailsResponse
import com.example.easyfood.data.models.response.MealListResponse
import com.example.easyfood.data.models.response.MealResponse
import com.example.easyfood.data.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragmentViewModel : ViewModel() {
    //    private var searchedMealLiveData = MutableLiveData<MealDetails>()
    private var searchedMealsLiveData = MutableLiveData<List<Meal>>()

//    fun searchMealDetail(name: String, context: Context?) {
//        RetrofitInstance.foodApi.getMealByName(name).enqueue(object : Callback<MealRandomResponse> {
//            override fun onResponse(
//                call: Call<MealRandomResponse>,
//                response: Response<MealRandomResponse>,
//            ) {
//                if (response.body()?.meals == null) {
//                    Toast.makeText(context?.applicationContext, "No such a meal", Toast.LENGTH_SHORT).show()
//                } else {
//                    val mealsList = response.body()?.meals
//                    mealsList?.let {
//                        searchedMealLiveData.postValue(it[0])
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<MealRandomResponse>, t: Throwable) {
//                Log.e("SearchFragmentVM", t.message.toString())
//            }
//        })
//    }

    fun searchMeals(searchQuery: String, context: Context?) =
        RetrofitInstance.foodApi.searchMeals(searchQuery).enqueue(
            object : Callback<MealResponse> {
                override fun onResponse(
                    call: Call<MealResponse>,
                    response: Response<MealResponse>,
                ) {
                    if (response.body()?.meals == null) {
                        Toast.makeText(context?.applicationContext,
                            "No such a meal",
                            Toast.LENGTH_SHORT).show()
                    } else {
                        val mealList = response.body()?.meals
                        mealList?.let {
                            searchedMealsLiveData.postValue(it)
                        }
                    }
                }

                override fun onFailure(call: Call<MealResponse>, t: Throwable) {
                    Log.e("SearchFragmentVM", t.message.toString())
                }
            }
        )

    fun observeSearchMealsLiveData(): LiveData<List<Meal>> {
        return searchedMealsLiveData
    }

//    fun observeSearchLiveData(): LiveData<MealDetails> {
//        return searchedMealLiveData
//    }
}