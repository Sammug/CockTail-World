package com.david.cocktailworld.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.david.cocktailworld.R
import com.david.cocktailworld.data.local.db.entities.Recipe
import com.david.cocktailworld.databinding.FragmentDrinkDetailsBinding
import com.david.cocktailworld.data.local.db.entities.Drink
import com.david.cocktailworld.ui.viewmodels.MainViewModel
import com.david.cocktailworld.utils.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DrinkDetailsFragment : Fragment() {
	private var _binding: FragmentDrinkDetailsBinding? = null
	val binding: FragmentDrinkDetailsBinding
	get() = _binding!!

	private val mainViewModel: MainViewModel by viewModels()

	private lateinit var recipe: Recipe

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
							drink?.map { _drink ->
								bindViews(_drink)
								createRecipe(_drink)
							}
						}
					}
					is Status.ERROR -> {
						Toast.makeText(requireContext(),"Error occured", Toast.LENGTH_LONG).show()
					}
					is Status.LOADING -> {
						//Toast.makeText(requireContext(),"LOADING..", Toast.LENGTH_LONG).show()
					}
				}
			})
		}

		binding.fabFavourite.setOnClickListener {
			mainViewModel.addToFavourites(recipe)
		}
	}

	private fun createRecipe(_drink: Drink) {
		recipe = Recipe(
			_drink.idDrink,
			_drink.strDrink,
			_drink.strTags,
			_drink.strCategory,
			_drink.strAlcoholic,
			_drink.strGlass,
			_drink.strInstructions,
			_drink.strDrinkThumb,
			_drink.strIngredient1,
			_drink.strIngredient2,
			_drink.strIngredient3,
			_drink.strIngredient4
		)
	}

	@SuppressLint("SetTextI18n")
	private fun bindViews(_drink: Drink) {
			binding.textViewTitle.text = _drink.strDrink
			binding.textViewTags.text = _drink.strTags
			binding.textViewCocktailType.text = _drink.strAlcoholic
			binding.textViewIngredient1.text = "O ${_drink.strIngredient1}"
			binding.textViewIngredient2.text = "O ${_drink.strIngredient2}"
			binding.textViewIngredient3.text = "O ${_drink.strIngredient3}"
			binding.textViewIngredient4.text = "O ${_drink.strIngredient4}"
			binding.textViewDrinkCategory.text = _drink.strCategory
			binding.textViewInstructions.text = _drink.strInstructions

			Glide.with(binding.root)
				.load(_drink.strDrinkThumb)
				.centerCrop()
				.error(R.drawable.cock_tail_thumbnail)
				.into(binding.imageViewDescription)
	}
}