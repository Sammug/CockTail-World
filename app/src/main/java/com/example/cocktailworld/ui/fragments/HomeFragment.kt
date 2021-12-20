package com.example.cocktailworld.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.cocktailworld.adapters.PopularDrinksAdapter
import com.example.cocktailworld.databinding.FragmentHomeBinding
import com.example.cocktailworld.model.Drinks
import com.example.cocktailworld.ui.viewmodels.MainViewModel
import com.example.cocktailworld.utils.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
	private var _binding: FragmentHomeBinding? = null
	val binding: FragmentHomeBinding
	get() = _binding!!

	private lateinit var adapter: PopularDrinksAdapter
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
		adapter = PopularDrinksAdapter(arrayListOf())

		binding.recyclerViewPopularDrink.layoutManager = LinearLayoutManager(requireContext()).also {
			LinearLayoutManager.HORIZONTAL
		}
		binding.recyclerViewPopularDrink.hasFixedSize()
		binding.recyclerViewPopularDrink.adapter = adapter

		mainViewModel.drinks.observe(viewLifecycleOwner, {
			when(it.status){
				Status.SUCCESS -> {
					it.data?.let {
						drinks -> loadDrinks(drinks)
					}
				}
				Status.ERROR -> {
					Toast.makeText(requireContext(),"Error occured",Toast.LENGTH_LONG).show()
				}
			}
		})

	}

	private fun loadDrinks(drinks: Drinks){
		adapter.addDrinks(drinks)
	}
}