package com.kakaogames.gd.ui.main.scores

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakaogames.gd.data.source.remote.repo.FootballRepositoryImpl
import com.kakaogames.gd.data.model.main.Matches
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScoresViewModel @Inject constructor(
    private val footballRepositoryImpl: FootballRepositoryImpl
) : ViewModel() {
    val uiState = MutableStateFlow<Exception?>(null)
    val scorers = MutableLiveData<List<Matches.Scorer?>?>()


    fun getMatches(id: Int) = viewModelScope.launch {
        try {
            val result = footballRepositoryImpl.getIdMatches(id)
            scorers.postValue(result.scorers)
        } catch (e: Exception) {
            uiState.value = e
        }
    }
}