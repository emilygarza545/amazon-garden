package com.Bibhavu3.Mamzon.domain.model

data class Zone(
    val id: ZoneId,
    val levelIds: List<Int>,
    val unlockedAtLevel: Int
)
