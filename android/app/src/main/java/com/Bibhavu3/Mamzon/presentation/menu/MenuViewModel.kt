package com.Bibhavu3.Mamzon.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Bibhavu3.Mamzon.data.sample.SampleData
import com.Bibhavu3.Mamzon.domain.model.GardenProgress
import com.Bibhavu3.Mamzon.domain.model.ZoneId
import com.Bibhavu3.Mamzon.domain.repository.GardenRepository
import java.time.LocalDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class MenuViewModel(
    private val repository: GardenRepository
) : ViewModel() {

    private val selectedZone = MutableStateFlow(
        SampleData.zoneOf(repository.progress.value.highestLevelUnlocked)
    )

    private val overlays = MutableStateFlow(MenuOverlays())

    private val _state = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(repository.progress, selectedZone, overlays) { progress, zone, overlay ->
                compose(progress, zone, overlay)
            }.collect { next ->
                _state.value = next
            }
        }
    }

    fun selectZone(zone: ZoneId) {
        if (!SampleData.isZoneUnlocked(zone, repository.progress.value.highestLevelUnlocked)) return
        selectedZone.value = zone
    }

    fun openAtlas() {
        overlays.value = overlays.value.copy(atlas = true)
    }

    fun closeAtlas() {
        overlays.value = overlays.value.copy(atlas = false)
    }

    fun openSettings() {
        overlays.value = overlays.value.copy(settings = true)
    }

    fun closeSettings() {
        overlays.value = overlays.value.copy(settings = false, resetConfirm = false)
    }

    fun askResetConfirm() {
        overlays.value = overlays.value.copy(resetConfirm = true)
    }

    fun cancelResetConfirm() {
        overlays.value = overlays.value.copy(resetConfirm = false)
    }

    fun confirmReset() {
        repository.clear()
        selectedZone.value = ZoneId.MIST_BASIN
        overlays.value = MenuOverlays()
    }

    fun buyDecoration(id: String) {
        val decoration = SampleData.decorations.firstOrNull { it.id == id } ?: return
        repository.update { progress ->
            if (progress.ownedDecorations.contains(id) || progress.totalSeeds < decoration.cost) {
                progress
            } else {
                progress.copy(
                    totalSeeds = progress.totalSeeds - decoration.cost,
                    ownedDecorations = progress.ownedDecorations + id,
                    equippedDecoration = id
                )
            }
        }
    }

    fun equipDecoration(id: String) {
        repository.update { progress ->
            if (!progress.ownedDecorations.contains(id)) {
                progress
            } else {
                progress.copy(equippedDecoration = if (progress.equippedDecoration == id) "" else id)
            }
        }
    }

    fun setSoundEnabled(enabled: Boolean) {
        repository.update { it.copy(soundEnabled = enabled) }
    }

    fun setHapticsEnabled(enabled: Boolean) {
        repository.update { it.copy(hapticsEnabled = enabled) }
    }

    fun setHintsEnabled(enabled: Boolean) {
        repository.update { it.copy(hintsEnabled = enabled) }
    }

    private fun compose(
        progress: GardenProgress,
        zone: ZoneId,
        overlay: MenuOverlays
    ): MenuUiState {
        val unlocked = SampleData.zones
            .map { it.id }
            .filter { SampleData.isZoneUnlocked(it, progress.highestLevelUnlocked) }
            .toSet()
        val activeZone = if (zone in unlocked) zone else ZoneId.MIST_BASIN
        return MenuUiState(
            loading = false,
            totalSeeds = progress.totalSeeds,
            levelsCleared = progress.levelsCleared,
            bestSeedsInRound = progress.bestSeedsInRound,
            selectedZone = activeZone,
            zoneOrder = SampleData.zones.map { it.id },
            unlockedZones = unlocked,
            nextLevelId = resolveLevel(activeZone, progress.highestLevelUnlocked),
            atlasSpecies = SampleData.atlasSpecies,
            discoveredSpecies = progress.discoveredSpecies,
            decorations = SampleData.decorations,
            ownedDecorations = progress.ownedDecorations,
            equippedDecoration = progress.equippedDecoration,
            soundEnabled = progress.soundEnabled,
            hapticsEnabled = progress.hapticsEnabled,
            hintsEnabled = progress.hintsEnabled,
            atlasVisible = overlay.atlas,
            settingsVisible = overlay.settings,
            resetConfirmVisible = overlay.resetConfirm
        )
    }

    private fun resolveLevel(zone: ZoneId, highestLevelUnlocked: Int): Int {
        if (zone == ZoneId.DAILY) {
            return SampleData.dailyLevel(LocalDate.now().dayOfYear).id
        }
        val ids = SampleData.zones.firstOrNull { it.id == zone }?.levelIds.orEmpty()
        return ids.lastOrNull { it <= highestLevelUnlocked } ?: ids.firstOrNull() ?: 1
    }
}

data class MenuOverlays(
    val atlas: Boolean = false,
    val settings: Boolean = false,
    val resetConfirm: Boolean = false
)
