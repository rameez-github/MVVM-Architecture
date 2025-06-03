package com.mvvm.example.data.room.dao


import androidx.room.Dao
import androidx.room.Query
import com.mvvm.example.data.room.Tables
import com.mvvm.example.data.room.model.StoreData
import kotlinx.coroutines.flow.Flow

/**
 * [Room] DAO for [StoreData] related operations.
 */
@Dao
abstract class StoreDao : BaseDao<StoreData> {
    @Query(
        """
        SELECT ${Tables.TABLE_STORES}.* FROM table_stores
        WHERE (store_name IS NULL OR store_name = '')
        OR (store_name LIKE '%' || :word || '%') 
        OR (address LIKE '%' || :word || '%') 
        OR (store_number LIKE '%' || :word || '%') 
        OR (road LIKE '%' || :word || '%')
        LIMIT :limit
        """
    )
    abstract fun getStores(
        word: String,
        limit: Int
    ): Flow<List<StoreData>>


    @Query("SELECT * FROM ${Tables.TABLE_STORES} WHERE store_name = :name")
    abstract suspend fun getStoreWithName(name: String): StoreData?

    @Query("SELECT * FROM ${Tables.TABLE_STORES} WHERE store_name = :name")
    abstract fun observeStoreData(name: String): Flow<StoreData?>


    @Query("DELETE FROM ${Tables.TABLE_STORES}")
    abstract fun deleteTable()

}