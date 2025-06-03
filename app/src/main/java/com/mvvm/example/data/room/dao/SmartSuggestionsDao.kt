package com.mvvm.example.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.mvvm.example.data.room.Tables
import com.mvvm.example.data.room.model.SmartSuggestionsData
import kotlinx.coroutines.flow.Flow

/**
 * [Room] DAO for [SmartSuggestionsData] related operations.
 */
@Dao
abstract class SmartSuggestionsDao : BaseDao<SmartSuggestionsData> {
    @Query(
        """
        SELECT ${Tables.TABLE_SMART_SUGGESTIONS}.* FROM table_smart_suggestions WHERE suggestion1 LIKE '%' || :word || '%'
        LIMIT :limit
        """
    )
    abstract fun getSmartSuggestions(
        word: String,
        limit: Int
    ): Flow<List<SmartSuggestionsData>>



    @Query("DELETE FROM ${Tables.TABLE_SMART_SUGGESTIONS}")
    abstract fun deleteTable()

}