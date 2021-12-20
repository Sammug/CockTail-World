package com.example.cocktailworld.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Drinks(
	val drinks: List<Drink>
): Parcelable
