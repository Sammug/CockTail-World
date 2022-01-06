package com.david.cocktailworld.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * This class serves as both remote data model
 * and a room data entity
 */

@Parcelize
@Entity(tableName = "drinks_table")
data class Drinks(
	@PrimaryKey(autoGenerate = true)
	val id: Int,
	val drinks: List<Drink>
): Parcelable
