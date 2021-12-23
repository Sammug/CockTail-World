package com.example.cocktailworld

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.cocktailworld.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
	private lateinit var _bindng: ActivityMainBinding
	val binding: ActivityMainBinding
	get() = _bindng
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		_bindng = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		val navHostFragment: NavHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
		val navController:NavController = navHostFragment.navController
		NavigationUI.setupWithNavController(binding.bottomNavigationView,navController)
	}
}