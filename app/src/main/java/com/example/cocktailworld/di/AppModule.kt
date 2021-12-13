package com.example.cocktailworld.di

import com.example.cocktailworld.data.api.ApiHelper
import com.example.cocktailworld.data.api.ApiService
import com.example.cocktailworld.utils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

	private val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
		level = HttpLoggingInterceptor.Level.BODY
	}

	@Provides
	@Singleton
	fun providesRetrofitClient(cache: Cache): OkHttpClient{
		return OkHttpClient.Builder()
		.addInterceptor(httpLoggingInterceptor)
		.connectTimeout(30, TimeUnit.SECONDS)
		.readTimeout(30, TimeUnit.SECONDS)
		.writeTimeout(30, TimeUnit.SECONDS)
			.addInterceptor(cacheInterceptor)
			.cache(cache).build()
	}



	@Provides
	@Singleton
	fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit
		.Builder()
		.baseUrl(BASE_URL)
		.client(okHttpClient)
		.addConverterFactory(GsonConverterFactory.create())
		.build()


	@Singleton
	@Provides
	fun providesApiService(retrofit: Retrofit) = retrofit.create(ApiService::class.java)

	@Singleton
	@Provides
	fun provideApiHelper(apiHelper: ApiHelper): ApiHelper = apiHelper

	private val cacheInterceptor = object : Interceptor {
		@Throws(IOException::class)
		override fun intercept(chain: Interceptor.Chain): Response {
			val response: Response = chain.proceed(chain.request())
			val cacheControl = CacheControl.Builder()
				.maxAge(30, TimeUnit.DAYS)
				.build()
			return response.newBuilder()
				.header("Cache-Control", cacheControl.toString())
				.build()
		}
	}
}