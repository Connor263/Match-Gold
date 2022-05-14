package com.kakaogames.gd.data.source.remote

import com.kakaogames.gd.data.model.main.CompetitionInfo
import com.kakaogames.gd.data.model.main.Matches
import com.kakaogames.gd.data.model.main.PlayerInfo
import com.kakaogames.gd.utils.API
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface FootballService {

    @Headers(
        "X-Auth-Token: $API"
    )
    @GET("v2/competitions/{id}")
    suspend fun getCompetition(@Path("id") id: String): CompetitionInfo.Competition


    @Headers(
        "X-Auth-Token: $API"
    )
    @GET("v2/competitions/{id}/scorers")
    suspend fun getIdMatches(@Path("id") id: Int): Matches


    @Headers(
        "X-Auth-Token: $API"
    )
    @GET("v2/players/{id}")
    suspend fun getPlayerInfo(@Path("id") id: Int): PlayerInfo

}