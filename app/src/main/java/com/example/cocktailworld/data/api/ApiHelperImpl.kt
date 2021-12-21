package com.example.cocktailworld.data.api

import com.example.cocktailworld.model.Drinks
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiHelperImpl @Inject constructor(
	private val apiService: ApiService
): ApiHelper{
	override suspend fun getPopularCockTails(): Response<Drinks> = apiService.getPopularCockTails()
	override suspend fun getMostLatestCockTails(): Response<Drinks> = apiService.getMostLatestCockTails()

}