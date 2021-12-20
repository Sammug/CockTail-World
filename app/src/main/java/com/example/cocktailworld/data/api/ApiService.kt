package com.example.cocktailworld.data.api

import com.example.cocktailworld.model.Drinks
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

	@GET("popular.php")
	suspend fun getPopularCockTails(): Response<Drinks>
}