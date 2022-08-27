package com.indra.foodrecipesapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indra.foodrecipesapp.data.RemoteDataStoreRepository
import com.indra.foodrecipesapp.models.FoodRecipe
import com.indra.foodrecipesapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: RemoteDataStoreRepository): ViewModel() {
    val foodRecipeResponse: MutableLiveData<Resource<FoodRecipe>> = MutableLiveData()
    fun getRecipes(queries: Map<String, String>) {
        viewModelScope.launch {
            when(val result = repository.getRecipes(queries)) {
                is Resource.Loading -> {
                    foodRecipeResponse.value = Resource.Loading(data = null)
                }
                is Resource.Success -> {
                    foodRecipeResponse.value = Resource.Success(data = result.data!!)
                }
                is Resource.Error -> {
                    foodRecipeResponse.value = Resource.Error(message = result.message!!)
                }
            }
        }
    }
}