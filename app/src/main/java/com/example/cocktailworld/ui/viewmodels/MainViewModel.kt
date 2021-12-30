package com.example.cocktailworld.ui.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.Network
import android.net.NetworkCapabilities.*
import android.os.Build
import android.widget.Toast
import androidx.lifecycle.*
import com.example.cocktailworld.HiltApplication
import com.example.cocktailworld.data.repositories.MainRepository
import com.example.cocktailworld.model.Drinks
import com.example.cocktailworld.utils.NetworkHelper
import com.example.cocktailworld.utils.NetworkStatus
import com.example.cocktailworld.utils.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
	private val mainRepository: MainRepository,
	application: Application
	): AndroidViewModel(application){
	private val _drinks = MutableLiveData<Resource<Drinks>>()
	val drinks: LiveData<Resource<Drinks>>
	get() = _drinks

	private val _latestDrinks = MutableLiveData<Resource<Drinks>>()
	val latestDrinks: LiveData<Resource<Drinks>>
		get() = _latestDrinks

	private val _topDrinks = MutableLiveData<Resource<Drinks>>()
	val topDrinks: LiveData<Resource<Drinks>>
		get() = _topDrinks

	private val _drink = MutableLiveData<Resource<Drinks>>()
	val drink: LiveData<Resource<Drinks>>
		get() = _drink

	val id: String? = null

	init {
		fetchPopularDrinks()
		fetchMostLatestDrinks()
		fetchRandomTopDrinks()
		id?.let { fetchDrinkDetails(it) }
	}

	 fun fetchPopularDrinks(){
		viewModelScope.launch {
			safePopularDrinksCall()
		}
	}

	fun fetchMostLatestDrinks(){
		viewModelScope.launch {
			safeLatestDrinksCall()
		}
	}

	fun fetchRandomTopDrinks(){
		viewModelScope.launch {
			safeRandomTopDrinksCall()
		}
	}

	fun fetchDrinkDetails(idDrink: String){
		viewModelScope.launch {
			_drink.postValue(Resource.loading(null))
			val response = mainRepository.getDrinkDetails(idDrink)
			_drink.postValue(handleDrinksResponse(response))
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

	private suspend fun safeRandomTopDrinksCall(){
		_topDrinks.postValue(Resource.loading(null))
		try {
			if (hasInternetConnection()){
				val response = mainRepository.getRandomTopCockTails()
				_topDrinks.postValue(handleDrinksResponse(response))
			}else{
				_topDrinks.postValue(Resource.error("No internet connection",null))
			}
		}catch (t: Throwable){
			when(t){
				is IOException -> _topDrinks.postValue(Resource.error("Network failure",null))
				else -> _topDrinks.postValue(Resource.error("Unexpected error occurred", null))
			}
		}

	}

	private suspend fun safePopularDrinksCall(){
		_drinks.postValue(Resource.loading(null))
		try {
			if (hasInternetConnection()){
				val response = mainRepository.getPopularCockTails()
				_drinks.postValue(handleDrinksResponse(response))
			}else{
				_drinks.postValue(Resource.error("No internet connection",null))
			}
		}catch (t: Throwable){
			when(t){
				is IOException -> _drinks.postValue(Resource.error("Network failure",null))
				else -> _drinks.postValue(Resource.error("Unexpected error occurred", null))
			}
		}

	}

	private suspend fun safeLatestDrinksCall(){
		_latestDrinks.postValue(Resource.loading(null))
		try {
			if (hasInternetConnection()){
				val response = mainRepository.getPopularCockTails()
				_latestDrinks.postValue(handleDrinksResponse(response))
			}else{
				_latestDrinks.postValue(Resource.error("No internet connection",null))
			}
		}catch (t: Throwable){
			when(t){
				is IOException -> _latestDrinks.postValue(Resource.error("Network failure",null))
				else -> _latestDrinks.postValue(Resource.error("Unexpected error occurred", null))
			}
		}

	}

	private fun hasInternetConnection(): Boolean {
		val connectivityManager = getApplication<HiltApplication>().getSystemService(
			Context.CONNECTIVITY_SERVICE
		) as ConnectivityManager
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			val activeNetwork = connectivityManager.activeNetwork ?: return false
			val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
			return when {
				capabilities.hasTransport(TRANSPORT_WIFI) -> true
				capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
				capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
				else -> false
			}
		} else {
			connectivityManager.activeNetworkInfo?.run {
				return when(type) {
					TYPE_WIFI -> true
					TYPE_MOBILE -> true
					TYPE_ETHERNET -> true
					else -> false
				}
			}
		}
		return false
	}
}