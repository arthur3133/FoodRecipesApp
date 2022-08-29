package com.indra.foodrecipesapp.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.indra.foodrecipesapp.data.database.RecipesEntity
import com.indra.foodrecipesapp.models.FoodRecipe
import com.indra.foodrecipesapp.util.Resource

class RecipesBinding {

    companion object {

        @BindingAdapter("readApiResponse", "readDatabase", requireAll = true)
        @JvmStatic
        fun  errorImageViewVisibility(imageView: ImageView, apiResponse: Resource<FoodRecipe>?, database: List<RecipesEntity>?) {
            if (apiResponse is Resource.Error && database.isNullOrEmpty()) {
                imageView.visibility = View.VISIBLE
            } else if (apiResponse is Resource.Loading) {
                imageView.visibility = View.INVISIBLE
            } else if (apiResponse is Resource.Success) {
                imageView.visibility = View.INVISIBLE
            }
        }

        @BindingAdapter("readApiResponse2", "readDatabase2", requireAll = true)
        @JvmStatic
        fun  errorTextViewVisibility(textView: TextView, apiResponse: Resource<FoodRecipe>?, database: List<RecipesEntity>?) {
            if (apiResponse is Resource.Error && database.isNullOrEmpty()) {
                textView.visibility = View.VISIBLE
                textView.text = apiResponse.message.toString()
            } else if (apiResponse is Resource.Loading) {
                textView.visibility = View.INVISIBLE
            } else if (apiResponse is Resource.Success) {
                textView.visibility = View.INVISIBLE
            }
        }
    }
}