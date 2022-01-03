package com.example.cocktailworld.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.cocktailworld.data.db.entities.Recipe
import com.example.cocktailworld.model.Drink
import com.example.cocktailworld.model.Drinks
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

@Dao
interface DrinksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrink(drinks: List<Drinks>)

    @Query("SELECT * FROM drinks_table")
    fun getDrinks(): Flow<List<Drinks>>

    @Query("DELETE FROM drinks_table")
    suspend fun deleteDrink()
}