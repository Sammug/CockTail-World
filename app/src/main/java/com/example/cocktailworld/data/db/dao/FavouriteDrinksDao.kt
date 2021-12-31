package com.example.cocktailworld.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.cocktailworld.data.db.entities.Recipe

@Dao
interface FavouriteDrinksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavDrink(recipe: Recipe)

    @Query("SELECT * FROM FAVOURITE_DRINKS")
    fun getAllFavDrinks(): LiveData<List<Recipe>>

    @Delete
    suspend fun deleteFavDrink(recipe: Recipe)
}