package com.kakaogames.gd.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakaogames.gd.data.source.remote.repo.FootballRepositoryImpl
import com.kakaogames.gd.data.model.main.CompetitionInfo
import com.kakaogames.gd.utils.listOfAvailableCompetition
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompetitionViewModel @Inject constructor(
    private val footballRepositoryImpl: FootballRepositoryImpl
) : ViewModel() {
    val uiState = MutableStateFlow<Exception?>(null)
    val competitions = MutableLiveData<List<CompetitionInfo.Competition>>()

    fun getCompetitions() = viewModelScope.launch {
        try {
            val result = mutableListOf<CompetitionInfo.Competition>()
            listOfAvailableCompetition.forEach {
                result.add(footballRepositoryImpl.getCompetition(id = it))
            }
            competitions.postValue(result)
        } catch (e: Exception) {
            uiState.value = e
        }
    }
}