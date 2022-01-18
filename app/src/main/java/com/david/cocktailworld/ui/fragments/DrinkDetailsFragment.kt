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
	private val binding: FragmentDrinkDetailsBinding
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
			mainViewModel.drink.observe(viewLifecycleOwner, { response ->
				when(response.status){
					Status.SUCCESS -> {
						response.data?.drinks.let { drinks ->
							drinks?.map { drink ->
								bindViews(drink)
								createRecipe(drink)
							}
						}
					}
					Status.ERROR -> {
						Toast.makeText(requireContext(),"Error occured", Toast.LENGTH_LONG).show()
					}
					Status.LOADING -> {
						//Toast.makeText(requireContext(),"LOADING..", Toast.LENGTH_LONG).show()
					}
				}
			})
		}

		binding.fabFavourite.setOnClickListener {
			mainViewModel.addToFavourites(recipe)
		}
	}

	private fun createRecipe(drink: Drink) {
		recipe = Recipe(
			drink.idDrink,
			drink.strDrink,
			drink.strTags,
			drink.strCategory,
			drink.strAlcoholic,
			drink.strGlass,
			drink.strInstructions,
			drink.strDrinkThumb,
			drink.strIngredient1,
			drink.strIngredient2,
			drink.strIngredient3,
			drink.strIngredient4
		)
	}

	@SuppressLint("SetTextI18n")
	private fun bindViews(drink: Drink) {
			binding.textViewTitle.text = drink.strDrink
			binding.textViewTags.text = drink.strTags
			binding.textViewCocktailType.text = drink.strAlcoholic
			binding.textViewIngredient1.text = "O ${drink.strIngredient1}"
			binding.textViewIngredient2.text = "O ${drink.strIngredient2}"
			binding.textViewIngredient3.text = "O ${drink.strIngredient3}"
			binding.textViewIngredient4.text = "O ${drink.strIngredient4}"
			binding.textViewDrinkCategory.text = drink.strCategory
			binding.textViewInstructions.text = drink.strInstructions

			Glide.with(binding.root)
				.load(drink.strDrinkThumb)
				.centerCrop()
				.error(R.drawable.cock_tail_thumbnail)
				.into(binding.imageViewDescription)
	}
}