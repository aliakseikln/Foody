package com.example.easyfood.data.models.response

import com.example.easyfood.data.models.Meal

data class MealListResponse(
    val meals: List<Meal>,
)