package com.example.cocktailworld.data.api

import com.example.cocktailworld.model.Drinks
import retrofit2.Response

interface ApiHelper {
	suspend fun getPopularCockTails(): Response<Drinks>
}