package com.Bibhavu3.Mamzon.domain.model

data class GardenProgress(
    val totalSeeds: Int = 0,
    val bestSeedsInRound: Int = 0,
    val levelsCleared: Int = 0,
    val highestLevelUnlocked: Int = 1,
    val discoveredSpecies: Set<String> = emptySet(),
    val ownedDecorations: Set<String> = emptySet(),
    val equippedDecoration: String = "",
    val soundEnabled: Boolean = true,
    val hapticsEnabled: Boolean = true,
    val hintsEnabled: Boolean = true
)
