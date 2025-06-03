package com.mvvm.example.data.room.model

import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mvvm.example.data.room.Tables

@Entity(
    tableName = Tables.TABLE_STORES
    /*,indices = [
        Index("name", unique = true)
    ]*/
)

@Immutable
data class StoreData(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "store_name") val store_name: String = "",
    @ColumnInfo(name = "store_number") val store_number: Int = 0,
    @ColumnInfo(name = "phone") val phone: String = "",
    @ColumnInfo(name = "address") val address: String = "",
    @ColumnInfo(name = "road") val road: String = "",
    @ColumnInfo(name = "latitude") val latitude: Double = 0.0,
    @ColumnInfo(name = "longitude") val longitude: Double = 0.0,
    @ColumnInfo(name = "store_image_path") val store_image_path: String = ""
)