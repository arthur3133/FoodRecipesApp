package com.indra.foodrecipesapp.viewmodels

import androidx.lifecycle.*
import com.indra.foodrecipesapp.data.DataStoreRepository
import com.indra.foodrecipesapp.data.LocalDataStoreRepository
import com.indra.foodrecipesapp.data.RemoteDataStoreRepository
import com.indra.foodrecipesapp.data.database.RecipesEntity
import com.indra.foodrecipesapp.models.FoodRecipe
import com.indra.foodrecipesapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val remoteDataStoreRepository: RemoteDataStoreRepository,
    private val localDataStoreRepository: LocalDataStoreRepository,
    private val dataStoreRepository: DataStoreRepository): ViewModel() {

//    DataStore
    val readMealAndDietType = dataStoreRepository.readMealAndDietType

    fun saveMealAndDietType(mealType: String, mealTypeId: Int, dietType: String, dietTypeId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveMealAndDietType(mealType, mealTypeId, dietType, dietTypeId)
        }
    }

//    Room Database
    val readRecipes: LiveData<List<RecipesEntity>> = localDataStoreRepository.readRecipes().asLiveData()

    private fun insertRecipes(recipesEntity: RecipesEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            localDataStoreRepository.insertRecipes(recipesEntity)
        }
    }

    private fun deleteRecipes() {
        viewModelScope.launch {
            localDataStoreRepository.deleteRecipes()
        }
    }

//    Retrofit
    val foodRecipeResponse: MutableLiveData<Resource<FoodRecipe>> = MutableLiveData()
    fun getRecipes(queries: Map<String, String>) {
        viewModelScope.launch {
            when(val result = remoteDataStoreRepository.getRecipes(queries)) {
                is Resource.Loading -> {
                    foodRecipeResponse.value = Resource.Loading(data = null)
                }
                is Resource.Success -> {
                    foodRecipeResponse.value = Resource.Success(data = result.data!!)
                    val foodRecipes = foodRecipeResponse.value!!.data
                    if (foodRecipes != null) {
                        offlineCacheRecipes(foodRecipes)
                    }
                }
                is Resource.Error -> {
                    foodRecipeResponse.value = Resource.Error(message = result.message!!)
                }
            }
        }
    }

    private fun offlineCacheRecipes(foodRecipes: FoodRecipe) {
        val recipesEntity = RecipesEntity(foodRecipes)
        deleteRecipes()
        insertRecipes(recipesEntity)
    }
}