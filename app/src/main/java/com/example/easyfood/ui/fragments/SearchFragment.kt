package com.example.easyfood.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.easyfood.MEAL_ID
import com.example.easyfood.MEAL_STR
import com.example.easyfood.MEAL_THUMB
import com.example.easyfood.databinding.FragmentSearchBinding
import com.example.easyfood.data.pojo.activites.MealDetailsActivity
import com.example.easyfood.ui.adapters.MealRecyclerAdapter
import com.example.easyfood.viewmodels.SearchViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private lateinit var myAdapter: MealRecyclerAdapter
    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchViewModel: SearchViewModel
    private var mealId = ""
    private var mealStr = ""
    private var mealThumb = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myAdapter = MealRecyclerAdapter()
        searchViewModel = ViewModelProviders.of(this)[SearchViewModel::class.java]
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

        onSearchClick()
        observeSearchLiveData()
        setOnMealCardClick()
        //    onEditTextSearchChange()
    }

    private fun onEditTextSearchChange() {
        var searchJob: Job? = null
        binding.edSearch.addTextChangedListener { searchQuery ->

            searchJob?.cancel()
            if (binding.edSearch.text.toString().isNotEmpty()) {
                searchJob = lifecycleScope.launch {
                    delay(400)
                    searchViewModel.searchMealDetail(searchQuery.toString(), context)
                }
            }
        }
    }

    private fun setOnMealCardClick() {
        binding.searchedMealCard.setOnClickListener {
            val intent = Intent(context, MealDetailsActivity::class.java)
            intent.putExtra(MEAL_ID, mealId)
            intent.putExtra(MEAL_STR, mealStr)
            intent.putExtra(MEAL_THUMB, mealThumb)
            startActivity(intent)
        }
    }

    private fun onSearchClick() {
        binding.icSearch.setOnClickListener {
            searchViewModel.searchMealDetail(binding.edSearch.text.toString(), context)
        }
    }

    private fun observeSearchLiveData() {
        searchViewModel.observeSearchLiveData().observe(viewLifecycleOwner) { t ->
                if (t == null) {
                    Toast.makeText(context, "No such a meal", Toast.LENGTH_SHORT).show()
                } else {
                    binding.apply {
                        mealId = t.idMeal
                        mealStr = t.strMeal
                        mealThumb = t.strMealThumb

                        context?.let {
                            Glide.with(it.applicationContext)
                                .load(t.strMealThumb)
                                .into(imgSearchedMeal)
                        }
                        tvSearchedMeal.text = t.strMeal
                        searchedMealCard.visibility = View.VISIBLE
                    }
                }
            }
    }
}