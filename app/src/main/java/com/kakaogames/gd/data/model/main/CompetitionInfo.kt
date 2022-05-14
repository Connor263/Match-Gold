package com.kakaogames.gd.data.model.main

data class CompetitionInfo(
    val competitions: List<Competition?>? = null,
    val count: Int? = null,
    val filters: Filters? = null
) {
    data class Competition(
        val area: Area? = null,
        val code: String? = null,
        val currentSeason: CurrentSeason? = null,
        val id: Int? = null,
        val lastUpdated: String? = null,
        val name: String? = null,
        val numberOfAvailableSeasons: Int? = null,
        val plan: String? = null
    ) {
        data class Area(
            val id: Int? = null,
            val name: String? = null
        )

        data class CurrentSeason(
            val currentMatchday: Int? = null,
            val endDate: String? = null,
            val id: Int? = null,
            val startDate: String? = null
        )
    }

    class Filters
}