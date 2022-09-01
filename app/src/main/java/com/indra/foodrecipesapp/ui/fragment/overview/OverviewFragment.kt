package com.indra.foodrecipesapp.ui.fragment.overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import coil.load
import com.indra.foodrecipesapp.R
import com.indra.foodrecipesapp.databinding.FragmentOverviewBinding
import com.indra.foodrecipesapp.models.Result
import com.indra.foodrecipesapp.util.Constants.RECIPE_RESULT_KEY
import org.jsoup.Jsoup

class OverviewFragment : Fragment() {
    private var _binding:FragmentOverviewBinding?=null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)
        val result: Result? = arguments?.getParcelable(RECIPE_RESULT_KEY)
        result?.let {
            binding.mainImageView.load(it.image) {
                crossfade(true)
            }
            binding.likesTextView.text = it.aggregateLikes.toString()
            binding.timeTextView.text = it.readyInMinutes.toString()
            binding.titleTextView.text = it.title
            if (it.vegan == true) {
                binding.veganImageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
                binding.veganTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            }
            if (it.vegetarian == true) {
                binding.vegetarianImageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
                binding.vegetarianTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            }
            if (it.glutenFree == true) {
                binding.glutenFreeImageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
                binding.glutenFreeTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            }
            if (it.dairyFree == true) {
                binding.dairyFreeImageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
                binding.dairyFreeTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            }
            if (it.veryHealthy == true) {
                binding.healthyImageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
                binding.healthyTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            }
            if (it.cheap == true) {
                binding.cheapImageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
                binding.cheapTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            }
            binding.summaryTextView.text = Jsoup.parse(it.summary).text()
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}