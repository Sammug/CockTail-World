package com.david.cocktailworld

import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.david.cocktailworld.databinding.ActivityMainBinding
import com.david.cocktailworld.utils.NetworkHelper
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
	private lateinit var _binding: ActivityMainBinding
	private val binding: ActivityMainBinding
	get() = _binding

	private lateinit var networkHelper: NetworkHelper

	private lateinit var snackBarView: Snackbar


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		networkHelper = NetworkHelper(this)
		_binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		networkHelper.observe(this,{ hasInternet ->
			when (hasInternet) {
				false -> {
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
						showStatusSnackBar("You are offline, turn on your cellular network or Wi-Fi and refresh")
					}
					binding.textViewOnlineOffline.visibility = View.VISIBLE
					binding.textViewOnlineOffline.text = resources.getString(R.string.offline)
				}
				true -> {
					binding.textViewOnlineOffline.visibility = View.GONE
				}
			}
		})

		val navHostFragment: NavHostFragment =
			supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
		val navController: NavController = navHostFragment.navController
		NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)

		//Hiding BottomNavigationView
		navController.addOnDestinationChangedListener(NavController.OnDestinationChangedListener { _, destination, _ ->
			when(destination.id){
				R.id.drinkDetailsFragment -> binding.bottomNavigationView.visibility = View.GONE
				else -> binding.bottomNavigationView.visibility = View.VISIBLE
			}
		})
	}

	@RequiresApi(Build.VERSION_CODES.M)
	private fun showStatusSnackBar(status: String) {
		snackBarView = Snackbar.make(binding.root, status, Snackbar.LENGTH_SHORT)
		val view  = snackBarView.view
		val params =  view.layoutParams as FrameLayout.LayoutParams
		params.gravity = Gravity.TOP
		view.layoutParams = params
		snackBarView.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
		snackBarView.show()
	}
}