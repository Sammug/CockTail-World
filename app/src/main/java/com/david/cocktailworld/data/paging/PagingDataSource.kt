package com.david.cocktailworld.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.david.cocktailworld.api.ApiService
import com.david.cocktailworld.data.local.db.entities.Drink
import com.david.cocktailworld.utils.STARTING_PAGE_INDEX
import retrofit2.HttpException
import java.io.IOException

class PagingDataSource(private val dataSource: ApiService): PagingSource<Int, Drink>(){
    override fun getRefreshKey(state: PagingState<Int, Drink>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Drink> {
        return try {
            val pageNumber = params.key ?: STARTING_PAGE_INDEX
            val response = dataSource.getPopularCockTails(params.loadSize,pageNumber)
            val drinks = response.drinks
            LoadResult.Page(
                data = drinks,
                prevKey = if (pageNumber == STARTING_PAGE_INDEX) null else pageNumber.minus(1),
                nextKey = if (response.drinks.isNotEmpty()) pageNumber.plus(1) else null
            )
        }catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}