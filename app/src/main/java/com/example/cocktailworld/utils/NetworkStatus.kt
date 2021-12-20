package com.example.cocktailworld.utils

sealed class NetworkStatus(val status:String){
	object Connected: NetworkStatus("connected")
	object Disconnected: NetworkStatus("disconnected")
}
