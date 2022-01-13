package com.david.cocktailworld.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.david.cocktailworld.model.Drink
import com.david.cocktailworld.model.Drinks
import kotlinx.coroutines.flow.Flow

@Dao
interface DrinksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrink(drink: List<Drink>)

    @Query("SELECT * FROM cocktails_table")
    fun getDrinks(): PagingSource<Int,Drink>

    @Query("DELETE FROM cocktails_table")
    suspend fun deleteDrink()
}