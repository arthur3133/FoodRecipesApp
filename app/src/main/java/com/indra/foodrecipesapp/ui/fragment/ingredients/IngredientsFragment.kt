package com.indra.foodrecipesapp.ui.fragment.ingredients

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.indra.foodrecipesapp.R
import com.indra.foodrecipesapp.adapters.IngredientsAdapter
import com.indra.foodrecipesapp.databinding.FragmentIngredientsBinding
import com.indra.foodrecipesapp.models.Result
import com.indra.foodrecipesapp.util.Constants

class IngredientsFragment : Fragment() {
    private var _binding: FragmentIngredientsBinding?=null
    private val binding get() = _binding!!
    private val adapter by lazy { IngredientsAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentIngredientsBinding.inflate(inflater, container, false)
        val result: Result? = arguments?.getParcelable(Constants.RECIPE_RESULT_KEY)
        result?.let {
            setupRecyclerView()
            adapter.setData(it.extendedIngredients)
        }
        return binding.root
    }

    private fun setupRecyclerView() {
        binding.ingredientRecyclerView.adapter = adapter
        binding.ingredientRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}