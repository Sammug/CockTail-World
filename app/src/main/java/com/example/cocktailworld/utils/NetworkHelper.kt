package com.example.cocktailworld.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress
import java.net.Socket
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkHelper @Inject constructor(@ApplicationContext private val context: Context):
	LiveData<NetworkStatus>(){
	val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
	val validateNetworkCapabilities: ArrayList<Network> = ArrayList()
	private lateinit var connectivityManagerCallback: ConnectivityManager.NetworkCallback

	fun getConnectivityManagerCallback() = object : ConnectivityManager.NetworkCallback(){
		override fun onAvailable(network: Network) {
			super.onAvailable(network)

			val networkCapability = connectivityManager.getNetworkCapabilities(network)
			val hasNetworkConnection = networkCapability?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)?:false
			if (hasNetworkConnection){
				confirmInternetConnection(network)
			}
		}

		override fun onLost(network: Network) {
			super.onLost(network)
			validateNetworkCapabilities.remove(network)
			displayStatus()
		}

		override fun onCapabilitiesChanged(
			network: Network,
			networkCapabilities: NetworkCapabilities
		) {
			super.onCapabilitiesChanged(network, networkCapabilities)

			if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)){
				confirmInternetConnection(network)
			}else{
				validateNetworkCapabilities.remove(network)
			}
			displayStatus()
		}
	}

	private fun displayStatus() {
		if (validateNetworkCapabilities.isNotEmpty()){
			postValue(NetworkStatus.Connected)
		}else{
			postValue(NetworkStatus.Disconnected)
		}
	}

	override fun onActive() {
		super.onActive()
		connectivityManagerCallback = getConnectivityManagerCallback()
		val networkRequest = NetworkRequest.Builder()
			.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
			.build()
		connectivityManager.registerNetworkCallback(networkRequest,connectivityManagerCallback)
	}

	override fun onInactive() {
		super.onInactive()
		connectivityManager.unregisterNetworkCallback(connectivityManagerCallback)
	}

	fun confirmInternetConnection(network: Network){
		CoroutineScope(Dispatchers.IO).launch {
			if (CheckInternetAvailability.check()){
				withContext(Dispatchers.Main){
					validateNetworkCapabilities.add(network)
					displayStatus()
				}
			}
		}
	}

	object CheckInternetAvailability {

		fun check() : Boolean {
			return try {
				val socket = Socket()
				socket.connect(InetSocketAddress("8.8.8.8",53))
				socket.close()
				true
			} catch ( e: Exception){
				e.printStackTrace()
				false
			}
		}

	}
}