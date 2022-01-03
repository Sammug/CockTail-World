package com.example.cocktailworld.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.cocktailworld.data.db.converters.RoomTypeConverter
import com.example.cocktailworld.data.db.dao.DrinksDao
import com.example.cocktailworld.data.db.dao.FavouriteDrinksDao
import com.example.cocktailworld.data.db.entities.Recipe
import com.example.cocktailworld.model.Drinks

@Database(
    entities = [Recipe::class, Drinks::class],
    version = 3
)
@TypeConverters(RoomTypeConverter::class)
abstract class DrinksDatabase: RoomDatabase() {
    abstract val favouriteDrinksDao: FavouriteDrinksDao
    abstract val drinksDao: DrinksDao

    companion object{
        @Volatile
        private var instance: DrinksDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance?: synchronized(LOCK){
            instance?: buildFavDatabase(
                context
            ).also {
                instance = it
            }
        }

        private fun buildFavDatabase(context: Context) = Room.databaseBuilder(
                context.applicationContext,
                DrinksDatabase::class.java,
                "favDrinks_db")
            .fallbackToDestructiveMigration()
                .build()
    }
}