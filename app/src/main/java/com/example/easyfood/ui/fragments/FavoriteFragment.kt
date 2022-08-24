package com.example.easyfood.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.easyfood.R
import com.example.easyfood.data.models.MealDataBase
import com.example.easyfood.databinding.FragmentFavoriteMealsBinding
import com.example.easyfood.ui.activities.DetailsActivity
import com.example.easyfood.ui.adapters.FavoriteRecyclerViewAdapter
import com.example.easyfood.utils.*
import com.example.easyfood.viewmodels.DetailsActivityViewModel
import com.google.android.material.snackbar.Snackbar


class FavoriteFragment : Fragment() {
    lateinit var recView: RecyclerView
    lateinit var fBinding: FragmentFavoriteMealsBinding
    private lateinit var myAdapter: FavoriteRecyclerViewAdapter
    private lateinit var detailsActivityViewModel: DetailsActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myAdapter = FavoriteRecyclerViewAdapter()
        detailsActivityViewModel = ViewModelProviders.of(this)[DetailsActivityViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        fBinding = FragmentFavoriteMealsBinding.inflate(inflater, container, false)
        return fBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView(view)
        onFavoriteMealClick()
        onFavoriteLongMealClick()
        observeBottomDialog()

        detailsActivityViewModel.observeSaveMeal().observe(viewLifecycleOwner) { t ->
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
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val favoriteMeal = myAdapter.getMelaByPosition(position)
                detailsActivityViewModel.deleteMeal(favoriteMeal)
                showDeleteSnackBar(favoriteMeal)
            }
        }
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recView)
    }

    private fun showDeleteSnackBar(favoriteMeal: MealDataBase) {
        Snackbar.make(requireView(), "Meal was deleted", Snackbar.LENGTH_LONG).apply {
            setAction("undo") {
                detailsActivityViewModel.insertMeal(favoriteMeal)
            }.show()
        }
    }

    private fun observeBottomDialog() {
        detailsActivityViewModel.observeMealBottomSheet().observe(viewLifecycleOwner) { t ->
            val bottomDialog = MealBottomDialogFragment()
            val b = Bundle()
            b.putString(CATEGORY_NAME, t!![0].strCategory)
            b.putString(MEAL_AREA, t[0].strArea)
            b.putString(MEAL_NAME, t[0].strMeal)
            b.putString(MEAL_THUMB, t[0].strMealThumb)
            b.putString(MEAL_ID, t[0].idMeal)
            bottomDialog.arguments = b
            bottomDialog.show(childFragmentManager, "Favorite bottom dialog")
        }
    }

    private fun prepareRecyclerView(v: View) {
        recView = v.findViewById(R.id.fav_rec_view)
        recView.adapter = myAdapter
        recView.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
    }

    private fun onFavoriteMealClick() {
        myAdapter.setOnFavoriteMealClickListener(object :
            FavoriteRecyclerViewAdapter.OnFavoriteClickListener {
            override fun onFavoriteClick(meal: MealDataBase) {
                val intent = Intent(context, DetailsActivity::class.java)
                intent.putExtra(MEAL_ID, meal.mealId.toString())
                intent.putExtra(MEAL_STR, meal.mealName)
                intent.putExtra(MEAL_THUMB, meal.mealThumb)
                startActivity(intent)
            }
        })
    }

    private fun onFavoriteLongMealClick() {
        myAdapter.setOnFavoriteLongClickListener(object :
            FavoriteRecyclerViewAdapter.OnFavoriteLongClickListener {
            override fun onFavoriteLongCLick(meal: MealDataBase) {
                detailsActivityViewModel.getMealByIdBottomSheet(meal.mealId.toString())
            }
        })
    }
}