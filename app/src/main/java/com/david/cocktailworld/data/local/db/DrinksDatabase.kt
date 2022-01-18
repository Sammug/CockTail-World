package com.david.cocktailworld.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.david.cocktailworld.data.local.db.dao.DrinksDao
import com.david.cocktailworld.data.local.db.dao.FavouriteDrinksDao
import com.david.cocktailworld.data.local.db.dao.RemoteKeysDao
import com.david.cocktailworld.data.local.db.entities.Recipe
import com.david.cocktailworld.data.local.db.entities.RemoteKeys
import com.david.cocktailworld.data.local.db.entities.Drink

@Database(
    entities = [Recipe::class, Drink::class, RemoteKeys::class],
    version = 5
)
abstract class DrinksDatabase: RoomDatabase() {
    abstract val favouriteDrinksDao: FavouriteDrinksDao
    abstract val drinksDao: DrinksDao
    abstract val remoteKeysDao: RemoteKeysDao

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