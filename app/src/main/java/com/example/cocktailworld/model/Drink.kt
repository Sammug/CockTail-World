package com.example.cocktailworld.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Drink(
	val idDrink: String,
	val strDrink: String,
	val strTags: String?,
	val strCategory: String,
	val strAlcoholic: String,
	val strGlass: String?,
	val strInstructions: String?,
	val strDrinkThumb: String
): Parcelable