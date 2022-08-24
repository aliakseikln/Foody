package com.example.easyfood.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.easyfood.data.models.Meal
import com.example.easyfood.data.models.MealDetails
import com.example.easyfood.databinding.MealCardBinding


class MealRecyclerViewAdapter : RecyclerView.Adapter<MealRecyclerViewAdapter.MealViewHolder>() {

    private var mealList: List<Meal> = ArrayList()
    private lateinit var setOnMealClickListener: SetOnMealClickListener

    fun setCategoryList(mealList: List<Meal>) {
        this.mealList = mealList
        notifyDataSetChanged()
    }

    fun setOnMealClickListener(setOnMealClickListener: SetOnMealClickListener) {
        this.setOnMealClickListener = setOnMealClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        return MealViewHolder(MealCardBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {

        holder.binding.apply {
            tvMealName.text = mealList[position].strMeal
            Glide.with(holder.itemView)
                .load(mealList[position].strMealThumb)
                .into(imgMeal)
        }

        holder.itemView.setOnClickListener {
            setOnMealClickListener.setOnClickListener(mealList[position])
        }
    }

    override fun getItemCount(): Int {
        return mealList.size
    }

    class MealViewHolder(val binding: MealCardBinding) : RecyclerView.ViewHolder(binding.root)

    interface SetOnMealClickListener {
        fun setOnClickListener(meal: Meal)
    }
}


