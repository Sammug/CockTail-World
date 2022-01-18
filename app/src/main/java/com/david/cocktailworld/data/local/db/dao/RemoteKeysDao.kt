package com.david.cocktailworld.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.david.cocktailworld.data.local.db.entities.RemoteKeys

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