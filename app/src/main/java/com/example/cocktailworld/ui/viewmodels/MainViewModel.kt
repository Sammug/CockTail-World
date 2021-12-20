package com.example.cocktailworld.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cocktailworld.data.repositories.MainRepository
import com.example.cocktailworld.model.Drinks
import com.example.cocktailworld.utils.ResourceStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
	private val mainRepository: MainRepository
): ViewModel(){
	private val _drinks = MutableLiveData<ResourceStatus<Drinks>>()
	val drinks: LiveData<ResourceStatus<Drinks>>
	get() = _drinks

	init {
		fetchPopularDrinks()
	}

	private fun fetchPopularDrinks(){
		viewModelScope.launch {
			_drinks.postValue(ResourceStatus.loading(null))
			mainRepository.getPopularCockTails().let {
				if (it.isSuccessful){
					_drinks.postValue(ResourceStatus.success(it.body()))
				}else{
					_drinks.postValue(ResourceStatus.error(it.errorBody().toString(),null))
				}
			}
		}
	}
}