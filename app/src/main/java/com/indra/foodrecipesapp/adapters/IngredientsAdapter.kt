package com.indra.foodrecipesapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.indra.foodrecipesapp.databinding.IngredientsRowLayoutBinding
import com.indra.foodrecipesapp.models.ExtendedIngredient
import com.indra.foodrecipesapp.util.FoodDiffUtil

class IngredientsAdapter: RecyclerView.Adapter<IngredientsAdapter.MyViewHolder>() {

    private var ingredients = emptyList<ExtendedIngredient>()
    class MyViewHolder(private val binding: IngredientsRowLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(ingredient: ExtendedIngredient) {
            binding.ingredient = ingredient
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = IngredientsRowLayoutBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentIngredient = ingredients[position]
        holder.bind(currentIngredient)
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    fun setData(newData: List<ExtendedIngredient>) {
        val result = FoodDiffUtil(ingredients, newData)
        val diffUtil = DiffUtil.calculateDiff(result)
        ingredients = newData
        diffUtil.dispatchUpdatesTo(this)
    }
}