package com.indra.foodrecipesapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indra.foodrecipesapp.data.Repository
import com.indra.foodrecipesapp.model.FoodRecipe
import com.indra.foodrecipesapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository): ViewModel() {
    private val foodRecipeResponse: MutableLiveData<Resource<FoodRecipe>> = MutableLiveData()

    fun getRecipes(queries: Map<String, String>) {
        viewModelScope.launch {
            getRecipesSafeCall(queries)
        }
    }

    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        Resource.Loading(data = null)
        try {
            val response = repository.remote.getRecipes(queries)
            foodRecipeResponse.value = handleFoodRecipesResponse(response)
        } catch (e: Exception) {
            Resource.Error(data = null, message = e.localizedMessage ?: "An unexpected error occurred.")
        }
    }

    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): Resource<FoodRecipe> {
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