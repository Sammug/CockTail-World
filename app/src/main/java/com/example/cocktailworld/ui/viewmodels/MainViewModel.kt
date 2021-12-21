package com.example.cocktailworld.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cocktailworld.data.repositories.MainRepository
import com.example.cocktailworld.model.Drinks
import com.example.cocktailworld.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
	private val mainRepository: MainRepository
): ViewModel(){
	private val _drinks = MutableLiveData<Resource<Drinks>>()
	val drinks: LiveData<Resource<Drinks>>
	get() = _drinks

	init {
		fetchPopularDrinks()
		fetchMostLatestDrinks()
		fetchRandomTopDrinks()
	}

	 fun fetchPopularDrinks(){
		viewModelScope.launch {
			_drinks.postValue(Resource.loading(null))
			val response = mainRepository.getPopularCockTails()
			_drinks.postValue(handleDrinksResponse(response))
		}
	}

	fun fetchMostLatestDrinks(){
		viewModelScope.launch {
			_drinks.postValue(Resource.loading(null))
			val response = mainRepository.getMostLatestCockTails()
			_drinks.postValue(handleDrinksResponse(response))
		}
	}

	fun fetchRandomTopDrinks(){
		viewModelScope.launch {
			_drinks.postValue(Resource.loading(null))
			val response = mainRepository.getRandomTopCockTails()
			_drinks.postValue(handleDrinksResponse(response))
		}
	}

	private fun handleDrinksResponse(response: Response<Drinks>): Resource<Drinks>{
		if (response.isSuccessful){
			response.body()?.let {drinks ->
				return Resource.success(drinks)
			}
		}
		return Resource.error(response.message(),null)
	}
}