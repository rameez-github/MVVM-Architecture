package com.mvvm.example.data.repository


import com.mvvm.example.data.room.dao.StoreDao
import com.mvvm.example.data.room.model.StoreData
import kotlinx.coroutines.flow.Flow


interface StoreRepository {
    /**
     * Returns a flow containing a list of stores which is sorted by the number
     * of podcasts in each store.
     */
    fun getStores(
        word: String,
        limit: Int = Integer.MAX_VALUE
    ): Flow<List<StoreData>>

    /**
     * Adds the store to the database if it doesn't already exist.
     *
     * @return the id of the newly inserted/existing store
     */
    suspend fun addStore(storeData: StoreData): Long
    suspend fun updateStore(storeData: StoreData)

    //suspend fun addPodcastToStore(podcastUri: String, storeId: Long)

    /**
     * @return gets the store with [name], if it exists, otherwise, null
     */
    fun getStore(name: String): Flow<StoreData?>
}

/**
 * A data repository for [StoreData] instances.
 */
class StoreRepositoryImp constructor(
    private val storeDao: StoreDao
) : StoreRepository {
    /**
     * Returns a flow containing a list of stores which is sorted by the number
     * of podcasts in each store.
     */
    override fun getStores(word: String, limit: Int): Flow<List<StoreData>> {
        return storeDao.getStores(word, limit)
    }

    /**
     * Adds the StoreData to the database if it doesn't already exist.
     *
     * @return the id of the newly inserted/existing store
     */
    override suspend fun addStore(storeData: StoreData): Long {
        return when (val local = storeDao.getStoreWithName(storeData.store_name)) {
            null -> storeDao.insert(storeData)
            else -> local.id
        }
    }

    /**
     * Adds the StoreData to the database if it doesn't already exist.
     *
     * @return the id of the newly inserted/existing store
     */
    override suspend fun updateStore(storeData: StoreData) {

        return storeDao.update(storeData)
    }

    override fun getStore(name: String): Flow<StoreData?> =
        storeDao.observeStoreData(name)
}