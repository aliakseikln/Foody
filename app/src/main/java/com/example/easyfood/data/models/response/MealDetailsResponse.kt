package com.example.easyfood.data.models.response

import com.example.easyfood.data.models.MealDetails

data class MealDetailsResponse(
    val mealDetailsList: List<MealDetails>,
)