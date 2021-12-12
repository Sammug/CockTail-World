package com.example.cocktailworld.data

import com.example.cocktailworld.data.api.ApiHelper
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiHelper: ApiHelper) {
	suspend fun getPopularCockTails() = apiHelper.getPopularCockTails()
}