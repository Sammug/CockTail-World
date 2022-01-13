package com.david.cocktailworld.data.mediators

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.david.cocktailworld.api.ApiService
import com.david.cocktailworld.data.db.DrinksDatabase
import com.david.cocktailworld.data.db.entities.RemoteKeys
import com.david.cocktailworld.model.Drink
import com.david.cocktailworld.utils.STARTING_PAGE_INDEX
import retrofit2.HttpException
import java.io.IOException

@ExperimentalPagingApi
class PopularDrinksRemoteMediator(
    private val apiService: ApiService,
    private val db: DrinksDatabase
): RemoteMediator<Int, Drink>(){
    override suspend fun load(loadType: LoadType, state: PagingState<Int, Drink>):
            MediatorResult {
        val loadKey = when (loadType) {
            LoadType.REFRESH -> null
            LoadType.PREPEND -> {
                return MediatorResult.Success(endOfPaginationReached = true)
            }
            LoadType.APPEND -> {
                db.withTransaction {
                    loadKey()
                }
            }
        }
        try {
            if (loadKey != null) {
                if (loadKey.isEndReached)
                    return MediatorResult.Success(endOfPaginationReached = true)
            }

            val page: Int = loadKey?.nextKey ?: STARTING_PAGE_INDEX
            val response = apiService.getPopularCockTails(
                state.config.pageSize, page
            )

            val endOfPaginationReached =
                response.pageData?.next_page == null || response.pageData.current_page == response.pageData.total_pages

            db.withTransaction {
                val nextPage = page + 1
                if (loadType == LoadType.REFRESH) {
                    db.drinksDao.deleteDrink()
                    db.remoteKeysDao.deleteKey()
                }
                db.remoteKeysDao.insertKey(
                    RemoteKeys(
                        0,
                        nextPage,
                        endOfPaginationReached
                    )
                )
                db.drinksDao.insertDrink(response.drinks)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun loadKey(): RemoteKeys? {
        return db.remoteKeysDao.getKeys().firstOrNull()
    }

}