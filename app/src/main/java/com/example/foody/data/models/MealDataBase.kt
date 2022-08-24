package com.example.foody.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal_information")
data class MealDataBase(
    @PrimaryKey
    val mealId: Int,
    val mealName: String,
    val mealCountry: String,
    val mealCategory: String,
    val mealInstruction: String,
    val mealThumb: String,
    val mealYoutubeLink: String,
)