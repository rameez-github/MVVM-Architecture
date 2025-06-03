package com.mvvm.example.data.room.model

import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mvvm.example.data.room.Tables

@Entity(
    tableName = Tables.TABLE_SMART_SUGGESTIONS
    /*,indices = [
        Index("name", unique = true)
    ]*/
)

@Immutable
data class SmartSuggestionsData(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "suggestion1") val suggestion1: String,
    @ColumnInfo(name = "suggestion2") val suggestion2: String,
    @ColumnInfo(name = "group_type") val group_type: String,
    @ColumnInfo(name = "priority") val priority: Int
)