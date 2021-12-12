package com.example.cocktailworld.data.api

import com.example.cocktailworld.model.Drink
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(
	private val apiService: ApiService
): ApiHelper{
	override suspend fun getPopularCockTails(): Response<List<Drink>> = apiService.getPopularCockTails()

}