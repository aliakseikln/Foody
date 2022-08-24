package com.example.foody.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foody.data.models.Meal
import com.example.foody.databinding.MostPopularCardBinding

class MostPopularRecyclerViewAdapter :
    RecyclerView.Adapter<MostPopularRecyclerViewAdapter.MostPopularMealViewHolder>() {
    private var mealsList: List<Meal> = ArrayList()
    private lateinit var onItemClick: OnItemClick
    private lateinit var onLongItemClick: OnLongItemClick

    fun setMealList(mealsList: List<Meal>) {
        this.mealsList = mealsList
        notifyDataSetChanged()
    }

    fun setOnClickListener(onItemClick: OnItemClick) {
        this.onItemClick = onItemClick
    }

    fun setOnLongCLickListener(onLongItemClick: OnLongItemClick) {
        this.onLongItemClick = onLongItemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MostPopularMealViewHolder {
        return MostPopularMealViewHolder(MostPopularCardBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: MostPopularMealViewHolder, position: Int) {
        holder.binding.apply {
            Glide.with(holder.itemView).load(mealsList[position].strMealThumb).into(imgPopularMeal)
        }

        holder.itemView.setOnClickListener {
            onItemClick.onItemClick(mealsList[position])
        }

        holder.itemView.setOnLongClickListener {
            onLongItemClick.onItemLongClick(mealsList[position])
            true
        }
    }

    override fun getItemCount(): Int {
        return mealsList.size
    }

    class MostPopularMealViewHolder(val binding: MostPopularCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface OnItemClick {
        fun onItemClick(meal: Meal)
    }

    interface OnLongItemClick {
        fun onItemLongClick(meal: Meal)
    }
}

