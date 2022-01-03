package com.example.cocktailworld.api

import com.example.cocktailworld.model.Drinks
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {

	@GET("popular.php")
	@Headers("Cacheable: true")
	suspend fun getPopularCockTails(): Response<Drinks>

	@GET("latest.php")
	suspend fun getMostLatestCockTails(): Response<Drinks>

	@GET("randomselection.php")
	suspend fun getRandomTopDrinks(): Response<Drinks>

	@GET("lookup.php")
	suspend fun getDrinkDetails(
		@Query("i") drinkId: String
	): Response<Drinks>
}