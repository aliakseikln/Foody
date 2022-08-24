package com.example.easyfood.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.easyfood.R
import com.example.easyfood.data.models.MealDataBase
import com.example.easyfood.databinding.FavMealCardBinding

class FavoriteRecyclerViewAdapter :
    RecyclerView.Adapter<FavoriteRecyclerViewAdapter.FavoriteViewHolder>() {
    private var favoriteMeals: List<MealDataBase> = ArrayList()
    private lateinit var onFavoriteClickListener: OnFavoriteClickListener
    private lateinit var onFavoriteLongClickListener: OnFavoriteLongClickListener

    fun setFavoriteMealsList(favoriteMeals: List<MealDataBase>) {
        this.favoriteMeals = favoriteMeals
        notifyDataSetChanged()
    }

    fun getMelaByPosition(position: Int): MealDataBase {
        return favoriteMeals[position]
    }


    fun setOnFavoriteMealClickListener(onFavoriteClickListener: OnFavoriteClickListener) {
        this.onFavoriteClickListener = onFavoriteClickListener
    }

    fun setOnFavoriteLongClickListener(onFavoriteLongClickListener: OnFavoriteLongClickListener) {
        this.onFavoriteLongClickListener = onFavoriteLongClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(FavMealCardBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val i = position
        holder.binding.apply {
            tvFavMealName.text = favoriteMeals[position].mealName
            Glide.with(holder.itemView)
                .load(favoriteMeals[position].mealThumb)
                .error(R.drawable.mealtest)
                .into(imgFavMeal)
        }

        holder.itemView.setOnClickListener {
            onFavoriteClickListener.onFavoriteClick(favoriteMeals[position])
        }

        holder.itemView.setOnLongClickListener {
            onFavoriteLongClickListener.onFavoriteLongCLick(favoriteMeals[i])
            true
        }
    }

    override fun getItemCount(): Int {
        return favoriteMeals.size
    }

    class FavoriteViewHolder(val binding: FavMealCardBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnFavoriteClickListener {
        fun onFavoriteClick(meal: MealDataBase)
    }

    interface OnFavoriteLongClickListener {
        fun onFavoriteLongCLick(meal: MealDataBase)
    }
}