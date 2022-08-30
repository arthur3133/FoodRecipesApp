package com.indra.foodrecipesapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.indra.foodrecipesapp.util.Constants.DEFAULT_DIET_TYPE
import com.indra.foodrecipesapp.util.Constants.DEFAULT_MEAL_TYPE
import com.indra.foodrecipesapp.util.Constants.PREFERENCES_DIET_TYPE
import com.indra.foodrecipesapp.util.Constants.PREFERENCES_MEAL_TYPE
import com.indra.foodrecipesapp.util.Constants.PREFERENCES_NAME
import com.indra.foodrecipesapp.util.Constants.PREFERENCES_DIET_TYPE_ID
import com.indra.foodrecipesapp.util.Constants.PREFERENCES_MEAL_TYPE_ID
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private object PreferenceKeys {
        val selectedMealType = stringPreferencesKey(PREFERENCES_MEAL_TYPE)
        val selectedMealTypeId = intPreferencesKey(PREFERENCES_MEAL_TYPE_ID)
        val selectedDietType = stringPreferencesKey(PREFERENCES_DIET_TYPE)
        val selectedDietTypeId = intPreferencesKey(PREFERENCES_DIET_TYPE_ID)
    }

    suspend fun saveMealAndDietType(
        mealType: String,
        mealTypeId: Int,
        dietType: String,
        dietTypeId: Int
    ) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.selectedMealType] = mealType
            preferences[PreferenceKeys.selectedMealTypeId] = mealTypeId
            preferences[PreferenceKeys.selectedDietType] = dietType
            preferences[PreferenceKeys.selectedDietTypeId] = dietTypeId
        }
    }

    val readMealAndDietType: Flow<MealAndDietType> =
        context.dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
            .map { preferences ->
                val selectedMealType =
                    preferences[PreferenceKeys.selectedMealType] ?: DEFAULT_MEAL_TYPE
                val selectedMealTypeId = preferences[PreferenceKeys.selectedMealTypeId] ?: 0
                val selectedDietType =
                    preferences[PreferenceKeys.selectedDietType] ?: DEFAULT_DIET_TYPE
                val selectedDietTypeId = preferences[PreferenceKeys.selectedDietTypeId] ?: 0
                MealAndDietType(
                    selectedMealType = selectedMealType,
                    selectedMealTypeId = selectedMealTypeId,
                    selectedDietType = selectedDietType,
                    selectedDietTypeId = selectedDietTypeId
                )
            }

}

data class MealAndDietType(
    val selectedMealType: String,
    val selectedMealTypeId: Int,
    val selectedDietType: String,
    val selectedDietTypeId: Int
)