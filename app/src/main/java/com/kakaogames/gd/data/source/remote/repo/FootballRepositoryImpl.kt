package com.kakaogames.gd.data.source.remote.repo

import com.kakaogames.gd.data.model.main.CompetitionInfo
import com.kakaogames.gd.data.model.main.Matches
import com.kakaogames.gd.data.model.main.PlayerInfo
import com.kakaogames.gd.data.source.remote.FootballService
import com.kakaogames.gd.interfaces.FootballRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FootballRepositoryImpl @Inject constructor(
    private val footballService: FootballService
) : FootballRepository {
    override suspend fun getCompetition(id: String): CompetitionInfo.Competition {
        return footballService.getCompetition(id)
    }

    override suspend fun getIdMatches(id: Int): Matches {
        return footballService.getIdMatches(id)
    }

    override suspend fun getPlayerInfo(id: Int): PlayerInfo {
        return footballService.getPlayerInfo(id)
    }
}