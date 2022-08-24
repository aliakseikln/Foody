package com.example.foody.data.models.response

import com.example.foody.data.models.MealDetails

data class MealDetailsResponse(
    val mealDetailsList: List<MealDetails>,
)