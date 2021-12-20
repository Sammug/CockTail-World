package com.example.cocktailworld.data.repositories

import com.example.cocktailworld.data.api.ApiHelper
import com.example.cocktailworld.data.api.ApiHelperImpl
import com.example.cocktailworld.data.api.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(private val apiService: ApiService) {
	suspend fun getPopularCockTails() = apiService.getPopularCockTails()
}