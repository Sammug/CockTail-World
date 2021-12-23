package com.example.cocktailworld.data.repositories

import com.example.cocktailworld.data.api.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(private val apiService: ApiService) {
	suspend fun getPopularCockTails() = apiService.getPopularCockTails()
	suspend fun getMostLatestCockTails() = apiService.getMostLatestCockTails()
	suspend fun getRandomTopCockTails() = apiService.getRandomTopDrinks()
	suspend fun getDrinkDetails(drinkId: String) = apiService.getDrinkDetails(drinkId)
}