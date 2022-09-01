package com.indra.foodrecipesapp.bindingadapters

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import com.indra.foodrecipesapp.R
import com.indra.foodrecipesapp.util.Constants.IMAGE_BASE_URL

class IngredientRowBinding {

    companion object {
        @BindingAdapter("loadIngredientImageFromUrl")
        @JvmStatic
        fun loadIngredientImageFromUrl(imageView: ImageView, imageName: String) {
            imageView.load(IMAGE_BASE_URL + imageName) {
                crossfade(true)
                error(R.drawable.ic_placehoder)
            }
        }

        @BindingAdapter("showIngredientAmount")
        @JvmStatic
        fun showIngredientAmount(textView: TextView, amount: Double) {
            textView.text = amount.toString()
        }
    }
}