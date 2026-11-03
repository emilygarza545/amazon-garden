package com.Bibhavu3.Mamzon.domain.model

data class RoundResult(
    val levelId: Int,
    val nextLevelId: Int,
    val zone: ZoneId,
    val outcome: Outcome,
    val hydrated: Int,
    val required: Int,
    val movesUsed: Int,
    val movesAllowed: Int,
    val seedsEarned: Int,
    val newBest: Boolean
)
