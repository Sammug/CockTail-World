package com.example.cocktailworld.model

import java.io.Serializable

data class Drink(
	val idDrink: String,
	val strDrink: String,
	val strTags: String?,
	val strCategory: String,
	val strAlcoholic: String,
	val strGlass: String?,
	val strInstructions: String?,
	val strDrinkThumb: String
): Serializable
