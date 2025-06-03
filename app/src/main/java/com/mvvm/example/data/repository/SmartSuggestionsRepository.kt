package com.mvvm.example.data.repository

import com.mvvm.example.data.room.dao.SmartSuggestionsDao
import com.mvvm.example.data.room.model.SmartSuggestionsData
import kotlinx.coroutines.flow.Flow

interface SmartSuggestionsRepository {
    /**
     * Returns a flow containing a list of suggestion which is sorted by the number
     * of podcasts in each suggestion.
     */
    fun getSmartSuggestions(
        word: String,
        limit: Int = 30//Integer.MAX_VALUE
    ): Flow<List<SmartSuggestionsData>>

}

/**
 * A data repository for [SmartSuggestionsData] instances.
 */
class SmartSuggestionsRepositoryImp constructor(
    private val suggestionsDao: SmartSuggestionsDao
) : SmartSuggestionsRepository {
    /**
     * Returns a flow containing a list of suggestion which is sorted by the number
     * of podcasts in each suggestion.
     */
    override fun getSmartSuggestions(word: String, limit: Int): Flow<List<SmartSuggestionsData>> {
        return suggestionsDao.getSmartSuggestions(word, limit)
    }

}