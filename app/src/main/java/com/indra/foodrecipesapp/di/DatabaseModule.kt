package com.indra.foodrecipesapp.di

import android.content.Context
import androidx.room.Room
import com.indra.foodrecipesapp.data.LocalDataStoreRepository
import com.indra.foodrecipesapp.data.database.RecipesDao
import com.indra.foodrecipesapp.data.database.RecipesDatabase
import com.indra.foodrecipesapp.util.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            RecipesDatabase::class.java,
            DATABASE_NAME
        ).build()

    @Singleton
    @Provides
    fun provideDao(recipesDatabase: RecipesDatabase) = recipesDatabase.recipesDao()

    @Singleton
    @Provides
    fun provideLocalDataStoreRepository(recipesDao: RecipesDao) = LocalDataStoreRepository(recipesDao)
}