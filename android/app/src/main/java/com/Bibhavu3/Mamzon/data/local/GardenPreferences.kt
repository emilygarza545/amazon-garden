package com.Bibhavu3.Mamzon.data.local

import android.content.Context
import android.content.SharedPreferences
import com.Bibhavu3.Mamzon.domain.model.GardenProgress

class GardenPreferences(context: Context) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences(STORE, Context.MODE_PRIVATE)

    fun read(): GardenProgress = GardenProgress(
        totalSeeds = preferences.getInt(KEY_TOTAL_SEEDS, 0),
        bestSeedsInRound = preferences.getInt(KEY_BEST_SEEDS, 0),
        levelsCleared = preferences.getInt(KEY_LEVELS_CLEARED, 0),
        highestLevelUnlocked = preferences.getInt(KEY_HIGHEST_LEVEL, 1),
        discoveredSpecies = preferences.getStringSet(KEY_SPECIES, emptySet()).orEmpty().toSet(),
        ownedDecorations = preferences.getStringSet(KEY_DECORATIONS, emptySet()).orEmpty().toSet(),
        equippedDecoration = preferences.getString(KEY_EQUIPPED, "").orEmpty(),
        soundEnabled = preferences.getBoolean(KEY_SOUND, true),
        hapticsEnabled = preferences.getBoolean(KEY_HAPTICS, true),
        hintsEnabled = preferences.getBoolean(KEY_HINTS, true)
    )

    fun write(progress: GardenProgress) {
        preferences.edit()
            .putInt(KEY_TOTAL_SEEDS, progress.totalSeeds)
            .putInt(KEY_BEST_SEEDS, progress.bestSeedsInRound)
            .putInt(KEY_LEVELS_CLEARED, progress.levelsCleared)
            .putInt(KEY_HIGHEST_LEVEL, progress.highestLevelUnlocked)
            .putStringSet(KEY_SPECIES, progress.discoveredSpecies)
            .putStringSet(KEY_DECORATIONS, progress.ownedDecorations)
            .putString(KEY_EQUIPPED, progress.equippedDecoration)
            .putBoolean(KEY_SOUND, progress.soundEnabled)
            .putBoolean(KEY_HAPTICS, progress.hapticsEnabled)
            .putBoolean(KEY_HINTS, progress.hintsEnabled)
            .apply()
    }

    fun wipe() {
        preferences.edit().clear().apply()
    }

    private companion object {
        const val STORE = "amazon_garden_progress"
        const val KEY_TOTAL_SEEDS = "total_seeds"
        const val KEY_BEST_SEEDS = "best_seeds_in_round"
        const val KEY_LEVELS_CLEARED = "levels_cleared"
        const val KEY_HIGHEST_LEVEL = "highest_level_unlocked"
        const val KEY_SPECIES = "discovered_species"
        const val KEY_DECORATIONS = "owned_decorations"
        const val KEY_EQUIPPED = "equipped_decoration"
        const val KEY_SOUND = "sound_enabled"
        const val KEY_HAPTICS = "haptics_enabled"
        const val KEY_HINTS = "hints_enabled"
    }
}
