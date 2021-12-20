package com.example.cocktailworld.di

import android.content.Context
import com.example.cocktailworld.BuildConfig
import com.example.cocktailworld.data.api.ApiHelper
import com.example.cocktailworld.data.api.ApiService
import com.example.cocktailworld.utils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
	@Provides
	@Singleton
	fun provideOkHttpClient(cache: Cache): OkHttpClient{
		val okHttpClient =  OkHttpClient.Builder()
		.connectTimeout(30, TimeUnit.SECONDS)
		.readTimeout(30, TimeUnit.SECONDS)
		.writeTimeout(30, TimeUnit.SECONDS)
			.addInterceptor(authInterceptor)
			.addInterceptor(cacheInterceptor)
			.cache(cache)
		if (BuildConfig.DEBUG){
			okHttpClient.addInterceptor(httpLoggingInterceptor)
		}
			return okHttpClient.build()
	}

	@Provides
	@Singleton
	fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit
		.Builder()
		.baseUrl(BASE_URL)
		.client(okHttpClient)
		.addConverterFactory(GsonConverterFactory.create())
		.build()


	@Singleton
	@Provides
	fun provideApiService(retrofit: Retrofit) = retrofit.create(ApiService::class.java)

	private val authInterceptor = Interceptor { chain ->
		val request: Request = chain.request().newBuilder()
			.addHeader("x-rapidapi-host", "the-cocktail-db.p.rapidapi.com")
			.addHeader("x-rapidapi-key", "e7c584bbb3msha47f13b481c1232p15ff0ajsnf86688373661")
			.build()
		chain.proceed(request)
	}

	private val cacheInterceptor = Interceptor { chain ->
		val response: Response = chain.proceed(chain.request())
		val cacheControl = CacheControl.Builder()
			.maxAge(30, TimeUnit.DAYS)
			.build()
		response.newBuilder()
			.header("Cache-Control", cacheControl.toString())
			.build()
	}

	/*Caching data */
	@Provides
	@Singleton
	fun provideCache(@ApplicationContext appContext: Context): Cache {

		return Cache(
			File(appContext.applicationContext.cacheDir, "cocktails_cache"),
			10 * 1024 * 1024
		)
	}

	private val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
		level = HttpLoggingInterceptor.Level.BODY
	}
}