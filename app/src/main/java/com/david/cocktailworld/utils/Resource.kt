package com.david.cocktailworld.utils


data class Resource<out T>(
	val status: Status,
	val data: T?,
	val message: String?
	){
	companion object ResourceStatus{
		fun <T> success(data: T?): Resource<T> {
			return Resource(Status.SUCCESS, data, null)
		}

		fun <T> error(msg: String, data: T?): Resource<T> {
			return Resource(Status.ERROR, null, msg)
		}

		fun <T> loading(data: T?): Resource<T> {
			return Resource(Status.LOADING, data, null)
		}
	}
}
sealed class Status{
	object SUCCESS: Status()
	object ERROR: Status()
	object LOADING: Status()
}
