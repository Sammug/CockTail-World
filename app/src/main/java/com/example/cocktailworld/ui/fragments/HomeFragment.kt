package com.example.cocktailworld.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cocktailworld.adapters.DrinksAdapter
import com.example.cocktailworld.databinding.FragmentHomeBinding
import com.example.cocktailworld.ui.viewmodels.MainViewModel
import com.example.cocktailworld.utils.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
	private var _binding: FragmentHomeBinding? = null
	val binding: FragmentHomeBinding
	get() = _binding!!

	private lateinit var adapter: DrinksAdapter
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

		adapter = DrinksAdapter()
		setRecyclerview()

		mainViewModel.drinks.observe(viewLifecycleOwner, {
			when(it.status){
				is Status.SUCCESS -> {
					it.data?.let {
						drinks -> adapter.differ.submitList(drinks.drinks)
					}
				}
				is Status.ERROR -> {
					Toast.makeText(requireContext(),"Error occured",Toast.LENGTH_LONG).show()
				}
				is Status.LOADING -> {
					Toast.makeText(requireContext(),"LOADING..",Toast.LENGTH_LONG).show()
				}
			}
		})
	}

	private fun setRecyclerview() {
		val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL,false)
		binding.recyclerViewPopularDrink.layoutManager = layoutManager
		binding.recyclerViewPopularDrink.hasFixedSize()
		binding.recyclerViewPopularDrink.adapter  = adapter
	}
}