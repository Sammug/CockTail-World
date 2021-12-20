package com.example.cocktailworld.utils


data class ResourceStatus<out T>(
	val status: Status,
	val data: T?,
	val message: String?
	){
	companion object LoadingStatus{
		fun <T> success(data: T?): ResourceStatus<T> {
			return ResourceStatus(Status.SUCCESS, data, null)
		}

		fun <T> error(msg: String, data: T?): ResourceStatus<T> {
			return ResourceStatus(Status.ERROR, null, msg)
		}

		fun <T> loading(data: T?): ResourceStatus<T> {
			return ResourceStatus(Status.LOADING, data, null)
		}
	}
}
sealed class Status{
	object SUCCESS: Status()
	object ERROR: Status()
	object LOADING: Status()
}
