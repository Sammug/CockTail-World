package com.david.cocktailworld.ui.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.david.cocktailworld.HiltApplication
import com.david.cocktailworld.data.db.entities.Recipe
import com.david.cocktailworld.data.repositories.MainRepository
import com.david.cocktailworld.model.Drinks
import com.david.cocktailworld.utils.NetworkHelper
import com.david.cocktailworld.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
	private val mainRepository: MainRepository,
	private val networkHelper: NetworkHelper,
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

	private val _onlineStatus = MutableSharedFlow<String>()
	val onlineStatus = _onlineStatus.asSharedFlow()

	val id: String? = null

	init {
		checkInternet()
		fetchPopularDrinks()
		fetchMostLatestDrinks()
		fetchRandomTopDrinks()
		id?.let { fetchDrinkDetails(it) }
	}
	private fun checkInternet(){
		viewModelScope.launch {
			val hasInternet: Boolean? = networkHelper.value
			hasInternet?.let {
				when(it){
					true -> {
						_onlineStatus.emit("online")
					}
					false -> {
						_onlineStatus.emit("disconnected")
					}
				}
			}
		}
	}

	 private fun fetchPopularDrinks(){
		viewModelScope.launch {
			safePopularDrinksCall()
		}
	}

	private fun fetchMostLatestDrinks(){
		viewModelScope.launch {
			safeLatestDrinksCall()
		}
	}

	private fun fetchRandomTopDrinks(){
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
				val response = mainRepository.getMostLatestCockTails()
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

	fun addToFavourites(recipe: Recipe) = viewModelScope.launch {
		mainRepository.addFavDrink(recipe)
	}

	fun getAllFavDrinks() = mainRepository.getAllFavDrinks()

	fun deleteFavRecipe(recipe: Recipe) = viewModelScope.launch {
		mainRepository.deleteFavDrink(recipe)
	}
}