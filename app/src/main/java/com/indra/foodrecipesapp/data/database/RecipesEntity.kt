package com.indra.foodrecipesapp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.indra.foodrecipesapp.models.FoodRecipe
import com.indra.foodrecipesapp.util.Constants.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class RecipesEntity(
    var foodRecipe: FoodRecipe
) {
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
}