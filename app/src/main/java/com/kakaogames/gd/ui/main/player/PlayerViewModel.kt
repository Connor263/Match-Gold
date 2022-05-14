package com.kakaogames.gd.ui.main.player

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakaogames.gd.data.source.remote.repo.FootballRepositoryImpl
import com.kakaogames.gd.data.model.main.PlayerInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val footballRepositoryImpl: FootballRepositoryImpl
) : ViewModel() {
    val uiState = MutableStateFlow<Exception?>(null)
    val playerInfo = MutableLiveData<PlayerInfo>()

    fun getPlayerInfo(id: Int) = viewModelScope.launch {
        try {
            val result = footballRepositoryImpl.getPlayerInfo(id)
            playerInfo.postValue(result)
        } catch (e: Exception) {
            uiState.value = e
        }
    }
}