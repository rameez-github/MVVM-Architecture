package com.mvvm.example.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mvvm.example.data.room.dao.SmartSuggestionsDao
import com.mvvm.example.data.room.dao.StoreDao
import com.mvvm.example.data.room.model.SmartSuggestionsData
import com.mvvm.example.data.room.model.StoreData


/**
 * The [RoomDatabase] we use in this app.
 */
@Database(
    entities = [
        StoreData::class,
        SmartSuggestionsData::class,
    ],
    version = 1,
    exportSchema = false,
)


@TypeConverters(DateTimeTypeConverters::class)
abstract class StoreFinderDatabase : RoomDatabase() {
    abstract fun storeDao(): StoreDao
    abstract fun smartSuggestionsDao(): SmartSuggestionsDao

    companion object {
        const val DATABASE_NAME = "store_finder_db"
        val migration_1_2 = object : Migration(1, 2) {

            override fun migrate(db: SupportSQLiteDatabase) {
                // Define SQL statements to handle schema migration
                db.execSQL("ALTER TABLE ${Tables.TABLE_STORES} ADD COLUMN store_number TEXT NOT NULL DEFAULT '0'")
            }
        }

        val migration_2_3 = object : Migration(2, 3) {

            override fun migrate(db: SupportSQLiteDatabase) {
                // Define SQL statements to handle schema migration
                db.execSQL("CREATE TABLE IF NOT EXISTS `${Tables.TABLE_SMART_SUGGESTIONS}` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `suggestion` TEXT NOT NULL)")
                db.execSQL("CREATE TABLE IF NOT EXISTS `User` (`id` INTEGER, PRIMARY KEY(`id`))")
            }
        }
    }

    fun deleteTables() {
        storeDao().deleteTable()
    }
}