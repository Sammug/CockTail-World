package com.david.cocktailworld.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.david.cocktailworld.data.db.entities.RemoteKeys
import com.david.cocktailworld.model.Drink
import com.david.cocktailworld.model.Drinks
import kotlinx.coroutines.flow.Flow

@Dao
interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKey(remoteKeys: RemoteKeys)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKeys(remoteKeys: List<RemoteKeys>)

    @Query("SELECT * FROM remote_keys_table")
   suspend fun getKeys(): List<RemoteKeys>

    @Query("DELETE FROM remote_keys_table")
    suspend fun deleteKey()
}