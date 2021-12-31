package com.example.cocktailworld.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cocktailworld.data.db.dao.FavouriteDrinksDao
import com.example.cocktailworld.data.db.entities.Recipe

@Database(
    entities = [Recipe::class],
    version = 2
)
abstract class FavDrinksDatabase: RoomDatabase() {
    abstract val favouriteDrinksDao: FavouriteDrinksDao

    companion object{
        private var instance: FavDrinksDatabase? = null
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
                FavDrinksDatabase::class.java,
                "favDrinks_db")
            .fallbackToDestructiveMigration()
                .build()
    }
}