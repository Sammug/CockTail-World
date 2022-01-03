package com.example.cocktailworld.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cocktailworld.R
import com.example.cocktailworld.adapters.LatestDrinksAdapter
import com.example.cocktailworld.adapters.PopularDrinksAdapter
import com.example.cocktailworld.adapters.TopTenRandomDrinksAdapter
import com.example.cocktailworld.databinding.FragmentHomeBinding
import com.example.cocktailworld.ui.viewmodels.MainViewModel
import com.example.cocktailworld.utils.Status
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
	private var _binding: FragmentHomeBinding? = null
	val binding: FragmentHomeBinding
	get() = _binding!!

	private lateinit var adapterPopular: PopularDrinksAdapter
	private lateinit var adapterLatest: LatestDrinksAdapter
	private lateinit var adapterTopTenDrinks: TopTenRandomDrinksAdapter
	private val mainViewModel: MainViewModel by viewModels()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		// Inflate the layout for this fragment
		_binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		adapterPopular = PopularDrinksAdapter{position ->  onPopularDrinkItemClicked(position)}
		adapterLatest = LatestDrinksAdapter { position -> onLatestItemClicked(position) }
		adapterTopTenDrinks = TopTenRandomDrinksAdapter{position -> onRandomDrinksItemClicked(position)}

		setPopularDrinksRecyclerview()
		setLatestDrinksRecyclerview()
		setTopDrinksViewPager()
		setObservables()
	}

	private fun setObservables() {
		binding.textViewNetworkStatus.visibility = View.GONE
		mainViewModel.drinks.observe(viewLifecycleOwner, {
			when(it.status){
				is Status.SUCCESS -> {
					it.data?.let { drinks ->
						adapterPopular.differ.submitList(drinks.drinks.subList(0,5))
					}
				}
				is Status.ERROR -> {
					it.message?.let {message ->
						binding.textViewNetworkStatus.visibility = View.VISIBLE
						binding.textViewNetworkStatus.text = message
					}
				}
				is Status.LOADING -> {
					//Toast.makeText(requireContext(),"LOADING..",Toast.LENGTH_LONG).show()
				}
			}
		})

		mainViewModel.latestDrinks.observe(viewLifecycleOwner, {
			when(it.status){
				is Status.SUCCESS -> {
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
					//Toast.makeText(requireContext(),"LOADING..",Toast.LENGTH_LONG).show()
				}
			}
		})

		mainViewModel.topDrinks.observe(viewLifecycleOwner, {
			when(it.status){
				is Status.SUCCESS -> {
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
					//Toast.makeText(requireContext(),"LOADING..",Toast.LENGTH_SHORT).show()
				}
			}
		})
	}

	private fun setTopDrinksViewPager() {
		//val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL,false)
//		binding.viewPagerTopTenDrinks.layoutManager = layoutManager
//		binding.recyclerViewTopTenDrinks.hasFixedSize()
		binding.viewPagerTopTenDrinks.adapter  = adapterTopTenDrinks

		TabLayoutMediator(binding.tabLayout,binding.viewPagerTopTenDrinks){tab,position ->

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
		binding.recyclerViewPopularDrink.adapter  = adapterPopular
	}

	private fun onLatestItemClicked(position: Int){
		val id = adapterLatest.differ.currentList[position].idDrink
		val bundle = bundleOf("ID" to id)
		val navController: NavController = binding.root.findNavController()
		navController.navigate(R.id.action_homeFragment2_to_drinkDetailsFragment, bundle)

	}

	private fun onPopularDrinkItemClicked(position: Int) {
		val id  = adapterPopular.differ.currentList[position].idDrink
		val bundle = bundleOf("ID" to id)
		val navController: NavController = binding.root.findNavController()
		navController.navigate(R.id.action_homeFragment2_to_drinkDetailsFragment,bundle)
	}

	private fun onRandomDrinksItemClicked(position: Int) {
		val id  = adapterTopTenDrinks.differ.currentList[position].idDrink
		val bundle = bundleOf("ID" to id)
		val navController: NavController = binding.root.findNavController()
		navController.navigate(R.id.action_homeFragment2_to_drinkDetailsFragment,bundle)
	}
}