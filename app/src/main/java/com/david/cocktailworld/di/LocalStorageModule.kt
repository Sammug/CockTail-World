package com.david.cocktailworld.di

import android.content.Context
import com.david.cocktailworld.data.local.db.DrinksDatabase
import com.david.cocktailworld.data.local.db.dao.FavouriteDrinksDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object LocalStorageModule {

    @Singleton
    @Provides
    fun provideDB(@ApplicationContext context: Context):DrinksDatabase{
        return DrinksDatabase.invoke(context)
    }

    @Singleton
    @Provides
    fun providesDao(
        drinksDatabase: DrinksDatabase
    ): FavouriteDrinksDao =
        drinksDatabase.favouriteDrinksDao
}