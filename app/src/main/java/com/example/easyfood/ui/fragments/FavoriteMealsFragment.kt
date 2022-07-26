package com.example.easyfood.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.easyfood.*
import com.example.easyfood.ui.adapters.FavoriteMealsRecyclerAdapter
import com.example.easyfood.data.pojo.MealDB
import com.example.easyfood.data.pojo.MealDetail
import com.example.easyfood.databinding.FragmentFavoriteMealsBinding
import com.example.easyfood.ui.MealBottomDialog
import com.example.easyfood.viewmodels.DetailsViewModel
import com.example.easyfood.data.pojo.activites.MealDetailsActivity
import com.example.easyfood.ui.adapters.OnFavoriteClickListener
import com.example.easyfood.ui.adapters.OnFavoriteLongClickListener
//import com.example.easyfood.util.Constants.Companion.CATEGORY_NAME
//import com.example.easyfood.util.Constants.Companion.MEAL_AREA
//import com.example.easyfood.util.Constants.Companion.MEAL_ID
//import com.example.easyfood.util.Constants.Companion.MEAL_NAME
//import com.example.easyfood.util.Constants.Companion.MEAL_STR
//import com.example.easyfood.util.Constants.Companion.MEAL_THUMB
import com.google.android.material.snackbar.Snackbar


class FavoriteMeals : Fragment() {
    lateinit var recView:RecyclerView
    lateinit var fBinding:FragmentFavoriteMealsBinding
    private lateinit var myAdapter:FavoriteMealsRecyclerAdapter
    private lateinit var detailsViewModel: DetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myAdapter = FavoriteMealsRecyclerAdapter()
        detailsViewModel = ViewModelProviders.of(this)[DetailsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fBinding = FragmentFavoriteMealsBinding.inflate(inflater,container,false)
        return fBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView(view)
        onFavoriteMealClick()
        onFavoriteLongMealClick()
        observeBottomDialog()

        detailsViewModel.observeSaveMeal().observe(viewLifecycleOwner) { t ->
            myAdapter.setFavoriteMealsList(t!!)
            if (t.isEmpty()) {
                fBinding.tvFavEmpty.visibility = View.VISIBLE
            } else {
                fBinding.tvFavEmpty.visibility = View.GONE
            }
        }

        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val favoriteMeal = myAdapter.getMelaByPosition(position)
                detailsViewModel.deleteMeal(favoriteMeal)
                showDeleteSnackBar(favoriteMeal)
            }
        }
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recView)
    }

    private fun showDeleteSnackBar(favoriteMeal:MealDB) {
        Snackbar.make(requireView(),"Meal was deleted",Snackbar.LENGTH_LONG).apply {
            setAction("undo") {
                detailsViewModel.insertMeal(favoriteMeal)
            }.show()
        }
    }

    private fun observeBottomDialog() {
        detailsViewModel.observeMealBottomSheet().observe(viewLifecycleOwner,object : Observer<List<MealDetail>>{
            override fun onChanged(t: List<MealDetail>?) {
                val bottomDialog = MealBottomDialog()
                val b = Bundle()
                b.putString(CATEGORY_NAME,t!![0].strCategory)
                b.putString(MEAL_AREA,t[0].strArea)
                b.putString(MEAL_NAME,t[0].strMeal)
                b.putString(MEAL_THUMB,t[0].strMealThumb)
                b.putString(MEAL_ID,t[0].idMeal)
                bottomDialog.arguments = b
                bottomDialog.show(childFragmentManager,"Favorite bottom dialog")
            }
        })
    }

    private fun prepareRecyclerView(v:View) {
        recView =v.findViewById(R.id.fav_rec_view)
        recView.adapter = myAdapter
        recView.layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
    }

    private fun onFavoriteMealClick(){
        myAdapter.setOnFavoriteMealClickListener(object : OnFavoriteClickListener {
            override fun onFavoriteClick(meal: MealDB) {
                val intent = Intent(context, MealDetailsActivity::class.java)
                intent.putExtra(MEAL_ID,meal.mealId.toString())
                intent.putExtra(MEAL_STR,meal.mealName)
                intent.putExtra(MEAL_THUMB,meal.mealThumb)
                startActivity(intent)
            }
        })
    }

    private fun onFavoriteLongMealClick() {
        myAdapter.setOnFavoriteLongClickListener(object : OnFavoriteLongClickListener {
            override fun onFavoriteLongCLick(meal: MealDB) {
                detailsViewModel.getMealByIdBottomSheet(meal.mealId.toString())
            }
        })
    }
}