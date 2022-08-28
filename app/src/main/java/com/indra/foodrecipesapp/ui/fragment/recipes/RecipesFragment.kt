package com.indra.foodrecipesapp.ui.fragment.recipes

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.indra.foodrecipesapp.viewmodels.MainViewModel
import com.indra.foodrecipesapp.adapters.RecipesAdapter
import com.indra.foodrecipesapp.databinding.FragmentRecipesBinding
import com.indra.foodrecipesapp.util.Constants.ADD_RECIPE_INFORMATION
import com.indra.foodrecipesapp.util.Constants.APIKEY
import com.indra.foodrecipesapp.util.Constants.API_KEY
import com.indra.foodrecipesapp.util.Constants.DIET
import com.indra.foodrecipesapp.util.Constants.FILLING_INGREDIENTS
import com.indra.foodrecipesapp.util.Constants.NUMBER
import com.indra.foodrecipesapp.util.Constants.TYPE
import com.indra.foodrecipesapp.util.Resource
import com.indra.foodrecipesapp.util.observeOnce
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesFragment : Fragment() {
    private var _binding: FragmentRecipesBinding?=null
    private val binding get() = _binding!!
    private val adapter: RecipesAdapter by lazy { RecipesAdapter() }
    private val mainViewModel: MainViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        setupRecyclerView()
        readDatabase()
        return binding.root
    }

    private fun setupRecyclerView() {
        binding.recipesRecyclerView.adapter = adapter
        binding.recipesRecyclerView.layoutManager = LinearLayoutManager(context)
        showProgressBar()
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observeOnce(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
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
            mainViewModel.getRecipes(getQueries())
            mainViewModel.foodRecipeResponse.observe(viewLifecycleOwner) { resourceResult ->
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
            Toast.makeText(requireContext(), "Please turn on internet", Toast.LENGTH_SHORT).show()
            hideProgressBar()
            binding.errorImageView.visibility = View.VISIBLE
            binding.errorTextView.visibility = View.VISIBLE
        }
    }

    private fun loadDataFromDatabase() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observe(viewLifecycleOwner) { database ->
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
        queries[NUMBER] = "50"
        queries[APIKEY] = API_KEY
        queries[TYPE] = "snack"
        queries[DIET] = "vegan"
        queries[ADD_RECIPE_INFORMATION] = "true"
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