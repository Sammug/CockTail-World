package com.david.cocktailworld.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.david.cocktailworld.model.Drinks
import kotlinx.coroutines.flow.Flow

@Dao
interface DrinksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrink(drinks: List<Drinks>)

    @Query("SELECT * FROM drinks_table")
    fun getDrinks(): Flow<List<Drinks>>

    @Query("DELETE FROM drinks_table")
    suspend fun deleteDrink()
}