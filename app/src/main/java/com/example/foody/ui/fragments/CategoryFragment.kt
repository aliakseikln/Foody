package com.example.foody.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foody.R
import com.example.foody.data.models.Category
import com.example.foody.databinding.FragmentCategoryBinding
import com.example.foody.ui.activities.MealActivity
import com.example.foody.ui.adapters.CategoryRecyclerViewAdapter
import com.example.foody.utils.CATEGORY_NAME
import com.example.foody.viewmodels.CategoryFragmentViewModel


class CategoryFragment : Fragment(R.layout.fragment_category) {
    private lateinit var binding: FragmentCategoryBinding
    private lateinit var categoryAdapter: CategoryRecyclerViewAdapter
    private lateinit var categoryViewModel: CategoryFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryAdapter = CategoryRecyclerViewAdapter()
        categoryViewModel = ViewModelProviders.of(this)[CategoryFragmentViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareRecyclerView()
        observeCategories()
        onCategoryClick()
    }

    private fun onCategoryClick() {
        categoryAdapter.onItemClicked(object : CategoryRecyclerViewAdapter.OnItemCategoryClicked {
            override fun onClickListener(category: Category) {
                val intent = Intent(context, MealActivity::class.java)
                intent.putExtra(CATEGORY_NAME, category.strCategory)
                startActivity(intent)
            }
        })
    }

    private fun observeCategories() {
        categoryViewModel.observeCategories()
            .observe(viewLifecycleOwner) { t ->
                categoryAdapter.setCategoryList(t!!)
            }
    }

    private fun prepareRecyclerView() {
        binding.favoriteRecyclerView.apply {
            adapter = categoryAdapter
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        }
    }
}