package com.indra.foodrecipesapp.data

import com.indra.foodrecipesapp.data.database.RecipesDao
import com.indra.foodrecipesapp.data.database.RecipesEntity
import com.indra.foodrecipesapp.models.FoodRecipe
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataStoreRepository @Inject constructor(private val recipesDao: RecipesDao) {

    fun readRecipes(): Flow<List<RecipesEntity>> {
        return recipesDao.readRecipes()
    }

    suspend fun insertRecipes(recipesEntity: RecipesEntity) {
        recipesDao.insertRecipes(recipesEntity)
    }

    suspend fun deleteRecipes() {
        recipesDao.deleteRecipes()
    }
}