package com.indra.foodrecipesapp.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.*
import com.indra.foodrecipesapp.data.DataStoreRepository
import com.indra.foodrecipesapp.data.LocalDataStoreRepository
import com.indra.foodrecipesapp.data.RemoteDataStoreRepository
import com.indra.foodrecipesapp.data.database.RecipesEntity
import com.indra.foodrecipesapp.models.FoodRecipe
import com.indra.foodrecipesapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val remoteDataStoreRepository: RemoteDataStoreRepository,
    private val localDataStoreRepository: LocalDataStoreRepository,
    private val dataStoreRepository: DataStoreRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    var networkStatus = false
    var backOnline = false

    //    DataStore
    val readMealAndDietType = dataStoreRepository.readMealAndDietType
    val readBackOnline = dataStoreRepository.readBackOnline.asLiveData()

    fun saveMealAndDietType(mealType: String, mealTypeId: Int, dietType: String, dietTypeId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveMealAndDietType(mealType, mealTypeId, dietType, dietTypeId)
        }
    }

    private fun saveBackOnline(backOnline: Boolean) {
        viewModelScope.launch {
            dataStoreRepository.saveBackOnline(backOnline)
        }
    }

    //    Room Database
    val readRecipes: LiveData<List<RecipesEntity>> =
        localDataStoreRepository.readRecipes().asLiveData()

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
            when (val result = remoteDataStoreRepository.getRecipes(queries)) {
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

    fun showNetworkStatus() {
        if (!networkStatus) {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show()
            saveBackOnline(true)
        } else if (networkStatus) {
            if (backOnline) {
                Toast.makeText(context, "We're back online", Toast.LENGTH_SHORT).show()
                saveBackOnline(false)
            }
        }
    }
}