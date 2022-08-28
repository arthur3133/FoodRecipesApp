package com.indra.foodrecipesapp.data

import android.content.ContentValues
import android.util.Log
import com.indra.foodrecipesapp.models.FoodRecipe
import com.indra.foodrecipesapp.data.remote.FoodRecipesApi
import com.indra.foodrecipesapp.util.Resource
import javax.inject.Inject

class RemoteDataStoreRepository @Inject constructor(private val foodRecipesApi: FoodRecipesApi) {

    suspend fun getRecipes(queries: Map<String, String>): Resource<FoodRecipe> {
        Resource.Loading(data = null)
        val response = foodRecipesApi.getRecipes(queries = queries)
        return when {
            response.message().toString().contains("timeout") -> {
                Resource.Error(data = null, message = "Timeout")
            }
            response.code() == 402 -> {
                Resource.Error(data = null, message = "Api key limited.")
            }
            response.body()!!.results.isEmpty() -> {
                Resource.Error(data = null, message = "Recipes not found.")
            }
            response.isSuccessful -> {
                Resource.Success(data = response.body()!!)
            }
            else -> {
                Resource.Error(data = null, message = response.message())
            }
        }
    }
}