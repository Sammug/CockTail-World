package com.david.cocktailworld.data.repositories

import com.david.cocktailworld.api.ApiService
import com.david.cocktailworld.data.db.DrinksDatabase
import com.david.cocktailworld.data.db.entities.Recipe
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(
	private val apiService: ApiService,
	private val favDrinksDatabase: DrinksDatabase
	) {
	suspend fun getPopularCockTails() = apiService.getPopularCockTails()
	suspend fun getMostLatestCockTails() = apiService.getMostLatestCockTails()
	suspend fun getRandomTopCockTails() = apiService.getRandomTopDrinks()
	suspend fun getDrinkDetails(drinkId: String) = apiService.getDrinkDetails(drinkId)
	suspend fun addFavDrink(recipe: Recipe) = favDrinksDatabase.favouriteDrinksDao.insertFavDrink(recipe)
	suspend fun deleteFavDrink(recipe: Recipe) = favDrinksDatabase.favouriteDrinksDao.deleteFavDrink(recipe)
	fun getAllFavDrinks() = favDrinksDatabase.favouriteDrinksDao.getAllFavDrinks()

}