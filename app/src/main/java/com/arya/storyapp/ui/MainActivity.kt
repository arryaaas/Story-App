package com.arya.storyapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.arya.storyapp.R
import com.arya.storyapp.databinding.ActivityMainBinding
import com.arya.storyapp.utils.gone
import com.arya.storyapp.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener{ _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment, R.id.mapsFragment, R.id.settingsFragment -> {
                    binding.bottomNavigation.visible()
                }
                else -> binding.bottomNavigation.gone()
            }
        }
    }
}