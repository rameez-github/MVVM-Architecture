package com.mvvm.example.activities

import androidx.lifecycle.ViewModel
import com.mvvm.example.data.UserStore
import com.mvvm.example.data.repository.SmartSuggestionsRepository
import com.mvvm.example.data.room.model.SmartSuggestionsData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


@HiltViewModel
class SmartInputViewModel @Inject constructor(
    private val suggestionsRepository: SmartSuggestionsRepository,
    val userStore: UserStore
): ViewModel() {

    val suggestionsList: Flow<List<SmartSuggestionsData>> = suggestionsRepository.getSmartSuggestions("")

}