package com.mvvm.example.ui.main

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvvm.example.data.UserStore
import com.mvvm.example.data.repository.StoreRepository
import com.mvvm.example.data.room.model.StoreData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val storeRepository: StoreRepository,
    val userStore: UserStore
): ViewModel() {

    val selectedStore: MutableState<StoreData?> = mutableStateOf(null)

    private val _searchQuery = MutableStateFlow("")
    private val _searchLimit = MutableStateFlow(Integer.MAX_VALUE)

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchResultsFlow: Flow<List<StoreData>> = _searchQuery
        .combine(_searchLimit) { query, limit ->
            storeRepository.getStores(query, limit)
        }
        .flatMapLatest { it }


    fun addStore(storeData: StoreData) {

        viewModelScope.launch {
            storeRepository.addStore(storeData)
        }

    }


    fun updateStore(storeData: StoreData) {

        viewModelScope.launch {
            storeRepository.updateStore(storeData)
        }

    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

}