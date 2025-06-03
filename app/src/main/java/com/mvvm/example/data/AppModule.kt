package com.mvvm.example.data

import android.content.Context
import androidx.room.Room
import com.mvvm.example.data.repository.SmartSuggestionsRepository
import com.mvvm.example.data.repository.SmartSuggestionsRepositoryImp
import com.mvvm.example.data.repository.StoreRepository
import com.mvvm.example.data.repository.StoreRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.mvvm.example.data.room.StoreFinderDatabase
import com.mvvm.example.data.room.dao.SmartSuggestionsDao
import com.mvvm.example.data.room.dao.StoreDao


@InstallIn(SingletonComponent::class)
@Module
class AppModule() {

    @Provides
    @Singleton
    fun provideAppContext (@ApplicationContext appContext: Context) : Context = appContext

    @Provides
    @Singleton
    fun provideUserDataStore (@ApplicationContext appContext: Context) : UserStore = UserStore (appContext)

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): StoreFinderDatabase =
        Room.databaseBuilder(context, StoreFinderDatabase::class.java, StoreFinderDatabase.DATABASE_NAME)
            // This is not recommended for normal apps, but the goal of this sample isn't to
            // showcase all of Room.
            .fallbackToDestructiveMigration()
            /*.addMigrations(migration_1_2)*/
            .build()


    @Provides
    @Singleton
    fun provideStoreDao(
        database: StoreFinderDatabase
    ): StoreDao = database.storeDao()

    @Provides
    @Singleton
    fun provideSmartSuggestionDao(
        database: StoreFinderDatabase
    ): SmartSuggestionsDao = database.smartSuggestionsDao()


    @Provides
    @Singleton
    fun provideStoreRepository(
        storeDao: StoreDao
    ): StoreRepository = StoreRepositoryImp(
        storeDao = storeDao
    )


    @Provides
    @Singleton
    fun provideSmartSuggestionRepository(
        suggestionsDao: SmartSuggestionsDao
    ): SmartSuggestionsRepository = SmartSuggestionsRepositoryImp(
        suggestionsDao = suggestionsDao
    )
}