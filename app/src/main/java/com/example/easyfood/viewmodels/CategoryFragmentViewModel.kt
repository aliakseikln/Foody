package com.example.easyfood.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.easyfood.data.models.response.CategoryResponse
import com.example.easyfood.data.models.Category
import com.example.easyfood.data.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryFragmentViewModel : ViewModel() {
    private var categories: MutableLiveData<List<Category>> = MutableLiveData<List<Category>>()

    init {
        getCategories()
    }

    private fun getCategories(){
        RetrofitInstance.foodApi.getCategories().enqueue(object : Callback<CategoryResponse>{
            override fun onResponse(call: Call<CategoryResponse>, response: Response<CategoryResponse>) {
                categories.value = response.body()!!.categories
            }

            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                Log.d(TAG,t.message.toString())
            }

        })
    }

    fun observeCategories():LiveData<List<Category>>{
        return categories
    }

    companion object {
        const val TAG = "CategoryFragmentViewModel"
    }
}