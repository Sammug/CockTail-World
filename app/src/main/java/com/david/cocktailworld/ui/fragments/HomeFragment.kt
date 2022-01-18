package com.david.cocktailworld.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.david.cocktailworld.R
import com.david.cocktailworld.adapters.LatestDrinksAdapter
import com.david.cocktailworld.adapters.TopFivePopularDrinksAdapter
import com.david.cocktailworld.adapters.TopTenRandomDrinksAdapter
import com.david.cocktailworld.databinding.FragmentHomeBinding
import com.david.cocktailworld.ui.viewmodels.MainViewModel
import com.david.cocktailworld.utils.Status
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
	private var _binding: FragmentHomeBinding? = null
	private val binding: FragmentHomeBinding
	get() = _binding!!

	private lateinit var navController: NavController
	private lateinit var adapterTopFivePopular: TopFivePopularDrinksAdapter
	private lateinit var adapterLatest: LatestDrinksAdapter
	private lateinit var adapterTopTenDrinks: TopTenRandomDrinksAdapter
	private val mainViewModel: MainViewModel by viewModels()


	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		// Inflate the layout for this fragment
		_binding = FragmentHomeBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		navController = binding.root.findNavController()

		binding.textViewPopular.isVisible = false
		binding.textViewMore.isVisible = false
		binding.textViewLatest.isVisible = false

		adapterTopFivePopular = TopFivePopularDrinksAdapter{ position ->  onPopularDrinkItemClicked(position)}
		adapterLatest = LatestDrinksAdapter { position -> onLatestItemClicked(position) }
		adapterTopTenDrinks = TopTenRandomDrinksAdapter{position -> onRandomDrinksItemClicked(position)}

		binding.textViewMore.setOnClickListener {
			navController.navigate(R.id.action_homeFragment2_to_createRecipeFragment2)
		}

		setPopularDrinksRecyclerview()
		setLatestDrinksRecyclerview()
		setTopDrinksViewPager()
		subscribeToObservables()
	}

	private fun subscribeToObservables() {
		binding.textViewNetworkStatus.visibility = View.GONE
		mainViewModel.drinks.observe(viewLifecycleOwner, {
			when(it.status){
				is Status.SUCCESS -> {
					binding.progressBar.isVisible = false
					binding.textViewPopular.isVisible = true
					binding.textViewMore.isVisible = true
					binding.textViewLatest.isVisible = true

					it.data?.let { drinks ->
						adapterTopFivePopular.differ.submitList(drinks.drinks.subList(0,5))
					}
				}
				is Status.ERROR -> {
					it.message?.let {message ->
						binding.textViewNetworkStatus.visibility = View.VISIBLE
						binding.textViewNetworkStatus.text = message
					}
				}
				is Status.LOADING -> {
					binding.progressBar.isVisible = true
					binding.textViewPopular.isVisible = false
					binding.textViewMore.isVisible = false
					binding.textViewLatest.isVisible = false
				}
			}
		})

		mainViewModel.latestDrinks.observe(viewLifecycleOwner, {
			when(it.status){
				is Status.SUCCESS -> {
					binding.progressBar.isVisible = false
					binding.textViewPopular.isVisible = true
					binding.textViewMore.isVisible = true
					binding.textViewLatest.isVisible = true
					it.data?.let { drinks ->
						adapterLatest.differ.submitList(drinks.drinks)
					}
				}
				is Status.ERROR -> {
					it.message?.let {message ->
						binding.textViewNetworkStatus.visibility = View.VISIBLE
						binding.textViewNetworkStatus.text = message
					}
				}
				is Status.LOADING -> {
					binding.progressBar.isVisible = true
					binding.textViewMore.isVisible = false
					binding.textViewPopular.isVisible = false
					binding.textViewLatest.isVisible = false
				}
			}
		})

		mainViewModel.topDrinks.observe(viewLifecycleOwner, {
			when(it.status){
				is Status.SUCCESS -> {
					binding.progressBar.isVisible = false
					it.data?.let { drinks ->
						adapterTopTenDrinks.differ.submitList(drinks.drinks)
					}
				}
				is Status.ERROR -> {
					it.message?.let {message ->
						binding.textViewNetworkStatus.visibility = View.VISIBLE
						binding.textViewNetworkStatus.text = message
					}
				}
				is Status.LOADING -> {
					binding.progressBar.isVisible = true
				}
			}
		})
	}

	private fun setTopDrinksViewPager() {
		binding.viewPagerTopTenDrinks.adapter  = adapterTopTenDrinks

		TabLayoutMediator(binding.tabLayout,binding.viewPagerTopTenDrinks){ _, _ ->

		}.attach()
	}

	private fun setLatestDrinksRecyclerview() {
		val layoutManager = GridLayoutManager(activity,2,GridLayoutManager.VERTICAL,false)
		binding.recyclerViewLatestDrinks.layoutManager = layoutManager
		binding.recyclerViewLatestDrinks.hasFixedSize()
		binding.recyclerViewLatestDrinks.adapter  = adapterLatest
	}

	private fun setPopularDrinksRecyclerview() {
		val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL,false)
		binding.recyclerViewPopularDrink.layoutManager = layoutManager
		binding.recyclerViewPopularDrink.hasFixedSize()
		binding.recyclerViewPopularDrink.adapter  = adapterTopFivePopular
	}

	private fun onLatestItemClicked(position: Int){
		val id = adapterLatest.differ.currentList[position].idDrink
		val bundle = bundleOf("ID" to id)
		val navController: NavController = binding.root.findNavController()
		navController.navigate(R.id.action_homeFragment2_to_drinkDetailsFragment, bundle)

	}

	private fun onPopularDrinkItemClicked(position: Int) {
		val id  = adapterTopFivePopular.differ.currentList[position].idDrink
		val bundle = bundleOf("ID" to id)
		val navController: NavController = binding.root.findNavController()
		navController.navigate(R.id.action_homeFragment2_to_drinkDetailsFragment,bundle)
	}

	private fun onRandomDrinksItemClicked(position: Int) {
		val id  = adapterTopTenDrinks.differ.currentList[position].idDrink
		val bundle = bundleOf("ID" to id)
		navController.navigate(R.id.action_homeFragment2_to_drinkDetailsFragment,bundle)
	}
}