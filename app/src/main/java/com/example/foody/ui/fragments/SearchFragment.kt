package com.example.foody.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foody.data.models.Meal
import com.example.foody.databinding.FragmentSearchBinding
import com.example.foody.ui.activities.DetailsActivity
import com.example.foody.ui.adapters.MealRecyclerViewAdapter
import com.example.foody.ui.adapters.MealRecyclerViewAdapter.SetOnMealClickListener
import com.example.foody.utils.MEAL_ID
import com.example.foody.utils.MEAL_STR
import com.example.foody.utils.MEAL_THUMB
import com.example.foody.viewmodels.SearchFragmentViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private lateinit var mealAdapter: MealRecyclerViewAdapter
    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchViewModel: SearchFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mealAdapter = MealRecyclerViewAdapter()
        searchViewModel = ViewModelProviders.of(this)[SearchFragmentViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onEditTextSearchChange()
        prepareRecyclerView()
        observeSearchMealsLiveData()

//        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//
//            }
//        })

        mealAdapter.setOnMealClickListener(object : SetOnMealClickListener {
            override fun setOnClickListener(meal: Meal) {
                val intent = Intent(context, DetailsActivity::class.java)
                intent.putExtra(MEAL_ID, meal.idMeal)
                intent.putExtra(MEAL_STR, meal.strMeal)
                intent.putExtra(MEAL_THUMB, meal.strMealThumb)
                startActivity(intent)
            }
        })
    }

    private fun observeSearchMealsLiveData() {
        searchViewModel.observeSearchMealsLiveData()
            .observe(viewLifecycleOwner) { t ->
                mealAdapter.setCategoryList(t)
            }
    }

    private fun prepareRecyclerView() {
        binding.rvSearchedMeals.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = mealAdapter
        }
    }

    override fun onPause() {
        super.onPause()
//        activity?.onBackPressed()
    }

    private fun onEditTextSearchChange() {
        var searchJob: Job? = null
        binding.edSearchBox.addTextChangedListener { searchQuery ->
            searchJob?.cancel()
            if (binding.edSearchBox.text.toString().isNotEmpty()) {
                binding.rvSearchedMeals.visibility = View.VISIBLE
                searchJob = lifecycleScope.launch {
                    delay(200)
                    searchViewModel.searchMeals(searchQuery.toString(), context)
                }
            } else {
                binding.rvSearchedMeals.visibility = View.GONE
            }
        }
    }
}