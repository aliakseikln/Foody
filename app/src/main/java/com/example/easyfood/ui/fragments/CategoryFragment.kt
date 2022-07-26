package com.example.easyfood.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.easyfood.CATEGORY_NAME
import com.example.easyfood.R
import com.example.easyfood.data.pojo.Category
import com.example.easyfood.databinding.FragmentCategoryBinding
import com.example.easyfood.data.pojo.activites.MealActivity
import com.example.easyfood.ui.adapters.CategoriesRecyclerAdapter
import com.example.easyfood.viewmodels.CategoryViewModel


class CategoryFragment : Fragment(R.layout.fragment_category) {
    private lateinit var binding: FragmentCategoryBinding
    private lateinit var myAdapter: CategoriesRecyclerAdapter
    private lateinit var categoryViewModel: CategoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myAdapter = CategoriesRecyclerAdapter()
        categoryViewModel = ViewModelProviders.of(this)[CategoryViewModel::class.java]
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
        myAdapter.onItemClicked(object : CategoriesRecyclerAdapter.OnItemCategoryClicked {
            override fun onClickListener(category: Category) {
                val intent = Intent(context, MealActivity::class.java)
                intent.putExtra(CATEGORY_NAME, category.strCategory)
                startActivity(intent)
            }
        })
    }

    private fun observeCategories() {
        categoryViewModel.observeCategories()
            .observe(viewLifecycleOwner, object : Observer<List<Category>> {
                override fun onChanged(t: List<Category>?) {
                    myAdapter.setCategoryList(t!!)
                }

            })
    }

    private fun prepareRecyclerView() {
        binding.favoriteRecyclerView.apply {
            adapter = myAdapter
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        }
    }
}