package com.david.cocktailworld.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
data class Drinks(
	val pageData: PageData?,
	val drinks: List<Drink>
): Parcelable
