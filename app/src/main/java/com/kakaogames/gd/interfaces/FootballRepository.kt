package com.kakaogames.gd.interfaces

import com.kakaogames.gd.data.model.main.CompetitionInfo
import com.kakaogames.gd.data.model.main.Matches
import com.kakaogames.gd.data.model.main.PlayerInfo

interface FootballRepository {
    suspend fun getCompetition(id: String): CompetitionInfo.Competition

    suspend fun getIdMatches(id: Int): Matches

    suspend fun getPlayerInfo(id: Int): PlayerInfo
}