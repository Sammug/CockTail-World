package com.david.cocktailworld.di

import android.content.Context
import com.david.cocktailworld.api.ApiService
import com.david.cocktailworld.data.local.db.DrinksDatabase
import com.david.cocktailworld.data.local.db.dao.FavouriteDrinksDao
import com.david.cocktailworld.utils.API_KEY
import com.david.cocktailworld.utils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
	@Provides
	@Singleton
	fun provideOkHttpClient(): OkHttpClient{
		val okHttpClient =  OkHttpClient.Builder()
		.connectTimeout(30, TimeUnit.SECONDS)
		.readTimeout(30, TimeUnit.SECONDS)
		.writeTimeout(30, TimeUnit.SECONDS)
			.addInterceptor(authInterceptor)
		if (com.david.cocktailworld.BuildConfig.DEBUG){
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
	fun provideDB(@ApplicationContext context: Context):DrinksDatabase{
		return DrinksDatabase.invoke(context)
	}


	@Singleton
	@Provides
	fun provideApiService(retrofit: Retrofit) = retrofit.create(ApiService::class.java)

	@Singleton
	@Provides
	fun providesDao(
		drinksDatabase: DrinksDatabase
	): FavouriteDrinksDao =
		drinksDatabase.favouriteDrinksDao

		private val authInterceptor = Interceptor { chain ->
			val request: Request = chain.request().newBuilder()
				.addHeader("x-rapidapi-host", "the-cocktail-db.p.rapidapi.com")
				.addHeader("x-rapidapi-key", API_KEY)
				.build()
			chain.proceed(request)
		}

	/*http logging interceptor*/
		private val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
			level = HttpLoggingInterceptor.Level.BODY
		}
}