package com.david.cocktailworld.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import javax.inject.Inject
import javax.inject.Singleton
import javax.net.SocketFactory

val TAG = "Connectivity-Manager"

@Singleton
class NetworkHelper @Inject constructor(@ApplicationContext private val context: Context):
	MutableLiveData<Boolean>(){
	val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
	val validNetworkCapabilities: MutableSet<Network> = HashSet()
	private lateinit var connectivityManagerCallback: ConnectivityManager.NetworkCallback

	private fun getConnectivityManagerCallback() = object : ConnectivityManager.NetworkCallback(){
		override fun onAvailable(network: Network) {
			super.onAvailable(network)
			Log.d(TAG, "onAvailable: $network")
			val networkCapability = connectivityManager.getNetworkCapabilities(network)
			val hasNetworkConnection = networkCapability?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
			Log.d(TAG, "onAvailable: ${network}, $hasNetworkConnection")
			if (hasNetworkConnection == true){
				confirmInternetConnection(network)
			}
		}

		override fun onLost(network: Network) {
			super.onLost(network)
			Log.d(TAG, "onLost: $network")
			validNetworkCapabilities.remove(network)
			postValue(false)
		}

		override fun onCapabilitiesChanged(
			network: Network,
			networkCapabilities: NetworkCapabilities
		) {
			super.onCapabilitiesChanged(network, networkCapabilities)

			if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)){
				confirmInternetConnection(network)
			}else{
				validNetworkCapabilities.remove(network)
				postValue(false)
				Log.d(TAG, "onCapabilitiesChanged. ${value}")
			}
		}
	}

	private fun checkCurrentValidNetworks() {
		postValue(validNetworkCapabilities.size > 0)
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
		Log.d(TAG,"CONFIRMING INTERNET CONNECTION $network")
		CoroutineScope(Dispatchers.IO).launch {
			val hasInternet = CheckInternetAvailability.check(network.socketFactory)
			if (hasInternet){
				withContext(Dispatchers.Main){
					Log.d(TAG, "onAvailable: adding network. ${network}")
					validNetworkCapabilities.add(network)
					postValue(true)
				}
			}
		}
	}

	object CheckInternetAvailability {
		fun check(socketFactory: SocketFactory) : Boolean {
			return try {
				val socket = socketFactory.createSocket() ?: throw IOException("Null socket.")
				socket.connect(InetSocketAddress("8.8.8.8",53), 1500)
				socket.close()
				true
			} catch ( e: Exception){
				e.printStackTrace()
				false
			}
		}

	}
}