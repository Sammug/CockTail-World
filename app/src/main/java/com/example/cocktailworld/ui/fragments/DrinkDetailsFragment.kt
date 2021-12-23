package com.example.cocktailworld.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.cocktailworld.R
import com.example.cocktailworld.databinding.FragmentDrinkDetailsBinding
import com.example.cocktailworld.model.Drink
import com.example.cocktailworld.model.Drinks
import com.example.cocktailworld.ui.viewmodels.MainViewModel
import com.example.cocktailworld.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import java.security.acl.Owner

@AndroidEntryPoint
class DrinkDetailsFragment : Fragment() {
	private var _binding: FragmentDrinkDetailsBinding? = null
	val binding: FragmentDrinkDetailsBinding
	get() = _binding!!

	private val mainViewModel:MainViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		arguments?.let {

		}
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		_binding = FragmentDrinkDetailsBinding.inflate(inflater,container,false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val drinkId: String? = arguments?.getString("ID")

		drinkId?.let {
			mainViewModel.fetchDrinkDetails(it)
			mainViewModel.drink.observe(viewLifecycleOwner, { drinks ->
				when(drinks.status){
					is Status.SUCCESS -> {
						drinks.data?.drinks.let { drink ->
							drink?.map { _drink -> bindViews(_drink)
							}
						}
					}
					is Status.ERROR -> {
						Toast.makeText(requireContext(),"Error occured", Toast.LENGTH_LONG).show()
					}
					is Status.LOADING -> {
						Toast.makeText(requireContext(),"LOADING..", Toast.LENGTH_LONG).show()
					}
				}
			})
		}

	}

	private fun bindViews(_drink: Drink) {
		binding.textViewTitle.text = _drink.strDrink
		binding.textViewTags.text = _drink.strTags
		binding.textViewCocktailType.text = _drink.strAlcoholic
		binding.textViewIngredient1.text = _drink.strIngredient1
		binding.textViewIngredient2.text = _drink.strIngredient2
		binding.textViewIngredient3.text = _drink.strIngredient3
		binding.textViewIngredient4.text = _drink.strIngredient4
		binding.textViewDrinkCategory.text = _drink.strCategory
		binding.textViewInstructions.text = _drink.strInstructions

		Glide.with(binding.root)
			.load(_drink.strDrinkThumb)
			.centerCrop()
			.error(R.drawable.cock_tail_thumbnail)
			.into(binding.imageViewDescription)
	}
}