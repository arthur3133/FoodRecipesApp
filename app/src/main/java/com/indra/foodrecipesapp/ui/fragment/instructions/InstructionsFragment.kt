package com.indra.foodrecipesapp.ui.fragment.instructions

import android.content.ContentResolver
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.indra.foodrecipesapp.R
import com.indra.foodrecipesapp.databinding.FragmentInstructionsBinding
import com.indra.foodrecipesapp.models.Result
import com.indra.foodrecipesapp.util.Constants

class InstructionsFragment : Fragment() {
    private var _binding:FragmentInstructionsBinding?=null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentInstructionsBinding.inflate(inflater, container, false)
        val result: Result? = arguments?.getParcelable(Constants.RECIPE_RESULT_KEY)
        result?.sourceUrl?.let {
            binding.webView.webViewClient = object : WebViewClient(){}
            binding.webView.loadUrl(it)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}