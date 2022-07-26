package com.example.easyfood.data.pojo.activites

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.easyfood.*
import com.example.easyfood.data.pojo.Meal
import com.example.easyfood.databinding.ActivityCategoriesBinding
import com.example.easyfood.ui.adapters.MealRecyclerAdapter
import com.example.easyfood.ui.adapters.SetOnMealClickListener
import com.example.easyfood.viewmodels.MealActivityViewModel


class MealActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoriesBinding
    private lateinit var mealActivityViewModel: MealActivityViewModel
    private lateinit var myAdapter: MealRecyclerAdapter
    private var categoryNme = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mealActivityViewModel = ViewModelProviders.of(this)[MealActivityViewModel::class.java]
        startLoading()
        prepareRecyclerView()
        mealActivityViewModel.getMealsByCategory(getCategory())
        mealActivityViewModel.observeMeal().observe(this) { t ->
            if (t == null) {
                hideLoading()
                Toast.makeText(applicationContext, "No meals in this category", Toast.LENGTH_SHORT)
                    .show()
                onBackPressed()
            } else {
                myAdapter.setCategoryList(t)
                binding.tvCategoryCount.text = categoryNme + " : " + t.size.toString()
                hideLoading()
            }
        }

        myAdapter.setOnMealClickListener(object : SetOnMealClickListener {
            override fun setOnClickListener(meal: Meal) {
                val intent = Intent(applicationContext, MealDetailsActivity::class.java)
                intent.putExtra(MEAL_ID, meal.idMeal)
                intent.putExtra(MEAL_STR, meal.strMeal)
                intent.putExtra(MEAL_THUMB, meal.strMealThumb)
                startActivity(intent)
            }
        })
    }

    private fun hideLoading() {
        binding.apply {
            loadingGifMeals.visibility = View.INVISIBLE
            mealRoot.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))
        }
    }

    private fun startLoading() {
        binding.apply {
            loadingGifMeals.visibility = View.VISIBLE
            mealRoot.setBackgroundColor(ContextCompat.getColor(applicationContext,
                R.color.g_loading))
        }
    }

    private fun getCategory(): String {
        val x = intent.getStringExtra(CATEGORY_NAME)!!
        categoryNme = x
        return x
    }

    private fun prepareRecyclerView() {
        myAdapter = MealRecyclerAdapter()
        binding.mealRecyclerview.apply {
            adapter = myAdapter
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        }
    }
}