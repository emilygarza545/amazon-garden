package com.Bibhavu3.Mamzon.presentation.menu

import com.Bibhavu3.Mamzon.domain.model.Decoration
import com.Bibhavu3.Mamzon.domain.model.PlantSpecies
import com.Bibhavu3.Mamzon.domain.model.ZoneId

data class MenuUiState(
    val loading: Boolean = true,
    val totalSeeds: Int = 0,
    val levelsCleared: Int = 0,
    val bestSeedsInRound: Int = 0,
    val selectedZone: ZoneId = ZoneId.MIST_BASIN,
    val zoneOrder: List<ZoneId> = emptyList(),
    val unlockedZones: Set<ZoneId> = emptySet(),
    val nextLevelId: Int = 1,
    val atlasSpecies: List<PlantSpecies> = emptyList(),
    val discoveredSpecies: Set<String> = emptySet(),
    val decorations: List<Decoration> = emptyList(),
    val ownedDecorations: Set<String> = emptySet(),
    val equippedDecoration: String = "",
    val soundEnabled: Boolean = true,
    val hapticsEnabled: Boolean = true,
    val hintsEnabled: Boolean = true,
    val atlasVisible: Boolean = false,
    val settingsVisible: Boolean = false,
    val resetConfirmVisible: Boolean = false
) {
    val statsVisible: Boolean = levelsCleared > 0 && bestSeedsInRound > 0
}
