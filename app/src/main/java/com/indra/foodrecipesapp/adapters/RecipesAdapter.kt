package com.indra.foodrecipesapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.indra.foodrecipesapp.databinding.RecipesRowLayoutBinding
import com.indra.foodrecipesapp.models.FoodRecipe
import com.indra.foodrecipesapp.models.Result
import com.indra.foodrecipesapp.util.FoodDiffUtil

class RecipesAdapter: RecyclerView.Adapter<RecipesAdapter.MyViewHolder>() {

    private var recipes = emptyList<Result>()
    class MyViewHolder(private val binding: RecipesRowLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(result: Result) {
            binding.result = result
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RecipesRowLayoutBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentRecipe = recipes[position]
        holder.bind(currentRecipe)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    fun setData(newData: FoodRecipe) {
        val foodDiffUtil = FoodDiffUtil(recipes, newData.results)
        val diffResult = DiffUtil.calculateDiff(foodDiffUtil)
        recipes = newData.results
        diffResult.dispatchUpdatesTo(this)
    }
}