package com.kakaogames.gd.data.model.main

data class Matches(
    val competition: Competition? = null,
    val count: Int? = null,
    val filters: Filters? = null,
    val scorers: List<Scorer?>? = null,
    val season: Season? = null
) {
    data class Competition(
        val area: Area? = null,
        val code: String? = null,
        val id: Int? = null,
        val lastUpdated: String? = null,
        val name: String? = null,
        val plan: String? = null
    ) {
        data class Area(
            val id: Int? = null,
            val name: String? = null
        )
    }

    class Filters

    data class Scorer(
        val numberOfGoals: Int? = null,
        val player: Player? = null,
        val team: Team? = null
    ) {
        data class Player(
            val countryOfBirth: String? = null,
            val dateOfBirth: String? = null,
            val firstName: String? = null,
            val id: Int? = null,
            val lastName: String? = null,
            val lastUpdated: String? = null,
            val name: String? = null,
            val nationality: String? = null,
            val position: String? = null
        )

        data class Team(
            val id: Int? = null,
            val name: String? = null
        )
    }

    data class Season(
        val availableStages: List<String?>? = null,
        val currentMatchday: Int? = null,
        val endDate: String? = null,
        val id: Int? = null,
        val startDate: String? = null
    )
}