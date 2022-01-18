package com.david.cocktailworld.data.remote

import android.os.Parcelable
import com.david.cocktailworld.data.local.db.entities.Drink
import com.david.cocktailworld.data.paging.PageData
import kotlinx.parcelize.Parcelize

@Parcelize
data class Drinks(
	val pageData: PageData?,
	val drinks: List<Drink>
): Parcelable
