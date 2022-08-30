package com.indra.foodrecipesapp.ui.fragment.recipes

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.indra.foodrecipesapp.R
import com.indra.foodrecipesapp.viewmodels.RecipesViewModel
import com.indra.foodrecipesapp.adapters.RecipesAdapter
import com.indra.foodrecipesapp.databinding.FragmentRecipesBinding
import com.indra.foodrecipesapp.util.Constants.QUERY_ADD_RECIPE_INFORMATION
import com.indra.foodrecipesapp.util.Constants.QUERY_API_KEY
import com.indra.foodrecipesapp.util.Constants.API_KEY
import com.indra.foodrecipesapp.util.Constants.DEFAULT_DIET_TYPE
import com.indra.foodrecipesapp.util.Constants.DEFAULT_MEAL_TYPE
import com.indra.foodrecipesapp.util.Constants.DEFAULT_RECIPES_NUMBER
import com.indra.foodrecipesapp.util.Constants.QUERY_DIET
import com.indra.foodrecipesapp.util.Constants.FILLING_INGREDIENTS
import com.indra.foodrecipesapp.util.Constants.QUERY_NUMBER
import com.indra.foodrecipesapp.util.Constants.QUERY_TYPE
import com.indra.foodrecipesapp.util.Resource
import com.indra.foodrecipesapp.util.observeOnce
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesFragment : Fragment() {
    private var _binding: FragmentRecipesBinding?=null
    private val binding get() = _binding!!
    private val adapter: RecipesAdapter by lazy { RecipesAdapter() }
    private val recipesViewModel: RecipesViewModel by viewModels()
    private var mealType = DEFAULT_MEAL_TYPE
    private var dietType = DEFAULT_DIET_TYPE
    private val args by navArgs<RecipesFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = recipesViewModel
        setupRecyclerView()
        readDatabase()

        binding.recipesFab.setOnClickListener {
            findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheet)
        }
        return binding.root
    }

    private fun setupRecyclerView() {
        binding.recipesRecyclerView.adapter = adapter
        binding.recipesRecyclerView.layoutManager = LinearLayoutManager(context)
        showProgressBar()
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            recipesViewModel.readRecipes.observeOnce(viewLifecycleOwner) { database ->
                if (database.isNotEmpty() && !args.backFromBottomSheet) {
                    adapter.setData(database[0].foodRecipe)
                    hideProgressBar()
                } else {
                    requestApiData()
                }
            }
        }
    }

    private fun requestApiData() {
        if (isOnline()) {
            recipesViewModel.getRecipes(getQueries())
            recipesViewModel.foodRecipeResponse.observe(viewLifecycleOwner) { resourceResult ->
                when(resourceResult) {
                    is Resource.Loading -> {
                        showProgressBar()
                    }
                    is Resource.Success -> {
                        hideProgressBar()
                        val recipes = resourceResult.data
                        if (recipes != null) {
                            adapter.setData(recipes)
                        }
                    }
                    is Resource.Error -> {
                        loadDataFromDatabase()
                        hideProgressBar()
                        Toast.makeText(requireContext(), resourceResult.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Toast.makeText(requireContext(), "No Internet Connection", Toast.LENGTH_SHORT).show()
            hideProgressBar()
        }
    }

    private fun loadDataFromDatabase() {
        lifecycleScope.launch {
            recipesViewModel.readRecipes.observe(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    adapter.setData(database[0].foodRecipe)
                }
            }
        }
    }

    private fun isOnline(): Boolean {
        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport (NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    private fun getQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        lifecycleScope.launch {
            recipesViewModel.readMealAndDietType.collect { value ->
                mealType = value.selectedMealType
                dietType = value.selectedDietType
            }
        }
        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_TYPE] = mealType
        queries[QUERY_DIET] = dietType
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[FILLING_INGREDIENTS] = "true"
        return queries
    }

    private fun showProgressBar() {
        binding.progressbar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressbar.visibility = View.INVISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}