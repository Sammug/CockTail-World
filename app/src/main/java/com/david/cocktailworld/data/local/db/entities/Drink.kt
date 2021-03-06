package com.david.cocktailworld.data.local.db.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "cocktails_table")
@Parcelize
data class Drink(
	@PrimaryKey(autoGenerate = false)
	val idDrink: String,
	val strDrink: String,
	val strTags: String?,
	val strCategory: String?,
	val strAlcoholic: String?,
	val strGlass: String?,
	val strInstructions: String?,
	val strDrinkThumb: String?,
	val strIngredient1: String?,
	val strIngredient2: String?,
	val strIngredient3: String?,
	val strIngredient4: String?
): Parcelable