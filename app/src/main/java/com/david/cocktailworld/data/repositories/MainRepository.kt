package com.david.cocktailworld.data.repositories

import androidx.paging.*
import com.david.cocktailworld.api.ApiService
import com.david.cocktailworld.data.paging.PagingDataSource
import com.david.cocktailworld.data.local.db.DrinksDatabase
import com.david.cocktailworld.data.local.db.entities.Recipe
import com.david.cocktailworld.data.paging.PopularDrinksRemoteMediator
import com.david.cocktailworld.data.local.db.entities.Drink
import kotlinx.coroutines.flow.Flow
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

	@ExperimentalPagingApi
	fun getPagedPopularDrinks(): Flow<PagingData<Drink>>{
		return Pager(
			config = PagingConfig(
				pageSize = 10,
				enablePlaceholders = false
			),
			remoteMediator = PopularDrinksRemoteMediator(
				apiService,favDrinksDatabase
			),
			pagingSourceFactory = {PagingDataSource(apiService)}
		).flow
	}

}