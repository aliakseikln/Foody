package com.example.foody.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.foody.R
import com.example.foody.data.models.Category
import com.example.foody.data.models.Meal
import com.example.foody.data.models.MealDetails
import com.example.foody.data.models.response.MealRandomResponse
import com.example.foody.databinding.FragmentHomeBinding
import com.example.foody.ui.activities.DetailsActivity
import com.example.foody.ui.activities.MealActivity
import com.example.foody.ui.adapters.CategoryRecyclerViewAdapter
import com.example.foody.ui.adapters.CategoryRecyclerViewAdapter.OnItemCategoryClicked
import com.example.foody.ui.adapters.MostPopularRecyclerViewAdapter
import com.example.foody.ui.adapters.MostPopularRecyclerViewAdapter.OnItemClick
import com.example.foody.ui.adapters.MostPopularRecyclerViewAdapter.OnLongItemClick
import com.example.foody.utils.*
import com.example.foody.viewmodels.DetailsActivityViewModel
import com.example.foody.viewmodels.HomeFragmentViewModel


class HomeFragment : Fragment() {
    private lateinit var meal: MealRandomResponse
    private lateinit var detailsViewModel: DetailsActivityViewModel
    private lateinit var homeViewModel: HomeFragmentViewModel
    private lateinit var categoriesAdapter: CategoryRecyclerViewAdapter
    private lateinit var popularAdapter: MostPopularRecyclerViewAdapter
    private lateinit var binding: FragmentHomeBinding
    private var randomMealId = ""
    private var observer: Observer<List<MealDetails>> = Observer {
        val bottomSheetFragment = MealBottomDialogFragment()
        val b = Bundle()
        b.putString(CATEGORY_NAME, it!![0].strCategory)
        b.putString(MEAL_AREA, it[0].strArea)
        b.putString(MEAL_NAME, it[0].strMeal)
        b.putString(MEAL_THUMB, it[0].strMealThumb)
        b.putString(MEAL_ID, it[0].idMeal)

        bottomSheetFragment.arguments = b
        bottomSheetFragment.show(parentFragmentManager, "BottomSheetDialog")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailsViewModel = ViewModelProviders.of(this)[DetailsActivityViewModel::class.java]
        homeViewModel = ViewModelProviders.of(this)[HomeFragmentViewModel::class.java]
        binding = FragmentHomeBinding.inflate(layoutInflater)
        categoriesAdapter = CategoryRecyclerViewAdapter()
        popularAdapter = MostPopularRecyclerViewAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoadingCase()
        preparePopularMeals()
        onRandomMealClick()
        onRandomLongClick()

        homeViewModel.observeMealByCategory().observe(viewLifecycleOwner) { t ->
            val meals = t!!.meals
            binding.tvOverPupItems.text = "over popular ${meals[0].category} meals:"
            setMealsByCategoryAdapter(meals)
            cancelLoadingCase()
        }

        homeViewModel.observeCategories().observe(viewLifecycleOwner) { t ->
            val categories = t!!.categories
            setCategoryAdapter(categories)
        }

        homeViewModel.observeRandomMeal().observe(viewLifecycleOwner) { t ->
            val mealImage = view.findViewById<ImageView>(R.id.img_random_meal)
            val imageUrl = t!!.meals[0].strMealThumb
            randomMealId = t.meals[0].idMeal
            Glide.with(this@HomeFragment).load(imageUrl).into(mealImage)
            meal = t
        }

        detailsViewModel.observeMealBottomSheet().observeForever(observer)

        popularAdapter.setOnClickListener(object : OnItemClick {
            override fun onItemClick(meal: Meal) {
                val intent = Intent(activity, DetailsActivity::class.java)
                intent.putExtra(MEAL_ID, meal.idMeal)
                intent.putExtra(MEAL_STR, meal.strMeal)
                intent.putExtra(MEAL_THUMB, meal.strMealThumb)
                startActivity(intent)
            }
        })

        categoriesAdapter.onItemClicked(object : OnItemCategoryClicked {
            override fun onClickListener(category: Category) {
                val intent = Intent(activity, MealActivity::class.java)
                intent.putExtra(CATEGORY_NAME, category.strCategory)
                startActivity(intent)
            }
        })

        popularAdapter.setOnLongCLickListener(object : OnLongItemClick {
            override fun onItemLongClick(meal: Meal) {
                detailsViewModel.getMealByIdBottomSheet(meal.idMeal)
            }
        })

        binding.imgSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        detailsViewModel.observeMealBottomSheet().removeObserver(observer)
    }

    private fun onRandomMealClick() {
        binding.randomMeal.setOnClickListener {
            val temp = meal.meals[0]
            val intent = Intent(activity, DetailsActivity::class.java)
            intent.putExtra(MEAL_ID, temp.idMeal)
            intent.putExtra(MEAL_STR, temp.strMeal)
            intent.putExtra(MEAL_THUMB, temp.strMealThumb)
            startActivity(intent)
        }
    }

    private fun onRandomLongClick() {
        binding.randomMeal.setOnLongClickListener {
            detailsViewModel.getMealByIdBottomSheet(randomMealId)
            true
        }
    }

    private fun showLoadingCase() {
        binding.apply {
            header.visibility = View.INVISIBLE
            tvWouldLikeToEat.visibility = View.INVISIBLE
            randomMeal.visibility = View.INVISIBLE
            tvOverPupItems.visibility = View.INVISIBLE
            recViewMealsPopular.visibility = View.INVISIBLE
            loadingGif.visibility = View.VISIBLE
            rootHome.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.g_loading))
        }
    }

    private fun cancelLoadingCase() {
        binding.apply {
            header.visibility = View.VISIBLE
            tvWouldLikeToEat.visibility = View.VISIBLE
            randomMeal.visibility = View.VISIBLE
            tvOverPupItems.visibility = View.VISIBLE
            recViewMealsPopular.visibility = View.VISIBLE
            loadingGif.visibility = View.INVISIBLE
            rootHome.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        }
    }

    private fun setMealsByCategoryAdapter(meals: List<Meal>) {
        popularAdapter.setMealList(meals)
    }

    private fun setCategoryAdapter(categories: List<Category>) {
        categoriesAdapter.setCategoryList(categories)
    }

    private fun preparePopularMeals() {
        binding.recViewMealsPopular.apply {
            adapter = popularAdapter
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
        }
    }
}