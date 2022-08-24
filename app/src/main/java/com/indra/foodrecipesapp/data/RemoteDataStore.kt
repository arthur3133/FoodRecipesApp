package com.indra.foodrecipesapp.data

import com.indra.foodrecipesapp.model.FoodRecipe
import com.indra.foodrecipesapp.data.remote.FoodRecipesApi
import retrofit2.Response
import javax.inject.Inject

class RemoteDataStore @Inject constructor(private val foodRecipesApi: FoodRecipesApi) {

    suspend fun getRecipes(queries: Map<String, String>): Response<FoodRecipe> {
        return foodRecipesApi.getRecipes(queries = queries)
    }
}