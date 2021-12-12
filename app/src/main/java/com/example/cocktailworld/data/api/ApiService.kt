package com.example.cocktailworld.data.api

import com.example.cocktailworld.model.Drink
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
	@GET("latest")
	suspend fun getPopularCockTails(): Response<List<Drink>>
}