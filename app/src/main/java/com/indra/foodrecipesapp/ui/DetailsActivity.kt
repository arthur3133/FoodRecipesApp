package com.indra.foodrecipesapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.indra.foodrecipesapp.R
import com.indra.foodrecipesapp.adapters.PagerAdapter
import com.indra.foodrecipesapp.databinding.ActivityDetailsBinding
import com.indra.foodrecipesapp.ui.fragment.ingredients.IngredientsFragment
import com.indra.foodrecipesapp.ui.fragment.instructions.InstructionsFragment
import com.indra.foodrecipesapp.ui.fragment.overview.OverviewFragment
import com.indra.foodrecipesapp.util.Constants.RECIPE_RESULT_KEY

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private val args by navArgs<DetailsActivityArgs>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val bundle = Bundle()
        bundle.putParcelable(RECIPE_RESULT_KEY, args.result)

        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(IngredientsFragment())
        fragments.add(InstructionsFragment())

        val titles = ArrayList<String>()
        titles.add("Overview")
        titles.add("Ingredients")
        titles.add("Instructions")

        val adapter = PagerAdapter(
            bundle,
            fragments,
            titles,
            supportFragmentManager
        )

        binding.viewPager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}