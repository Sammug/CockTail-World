package com.example.cocktailworld

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.text.style.AlignmentSpan
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.cocktailworld.databinding.ActivityMainBinding
import com.example.cocktailworld.ui.viewmodels.MainViewModel
import com.example.cocktailworld.utils.NetworkHelper
import com.example.cocktailworld.utils.NetworkStatus
import com.example.cocktailworld.utils.TAG
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
	private lateinit var _bindng: ActivityMainBinding
	val binding: ActivityMainBinding
	get() = _bindng

	private lateinit var networkHelper: NetworkHelper
	private val viewModel: MainViewModel by viewModels()

	private var snackBarView: Snackbar? = null


	@RequiresApi(Build.VERSION_CODES.M)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		networkHelper = NetworkHelper(this)
		_bindng = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		networkHelper.observe(this,{ hasInternet ->
			when (hasInternet) {
				false -> {
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
	}

	@RequiresApi(Build.VERSION_CODES.M)
	private fun showStatusSnackBar(status: String?) {
		snackBarView = status?.let { Snackbar.make(binding.root, it, Snackbar.LENGTH_INDEFINITE) }
		val view  = snackBarView!!.view
		val params =  view.layoutParams as FrameLayout.LayoutParams
		params.gravity = Gravity.TOP
		view.layoutParams = params
		snackBarView!!.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
	}
}