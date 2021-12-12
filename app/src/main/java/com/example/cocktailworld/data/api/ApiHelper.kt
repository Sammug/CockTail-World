package com.example.cocktailworld.data.api

import com.example.cocktailworld.model.Drink
import retrofit2.Response

interface ApiHelper {
	suspend fun getPopularCockTails(): Response<List<Drink>>
}