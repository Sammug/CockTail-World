package com.david.cocktailworld.data.local.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.david.cocktailworld.data.local.db.entities.Recipe

@Dao
interface FavouriteDrinksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavDrink(recipe: Recipe)

    @Query("SELECT * FROM FAVOURITE_DRINKS")
    fun getAllFavDrinks(): LiveData<List<Recipe>>

    @Delete
    suspend fun deleteFavDrink(recipe: Recipe)
}