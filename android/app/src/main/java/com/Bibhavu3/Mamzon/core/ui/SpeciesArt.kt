package com.Bibhavu3.Mamzon.core.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.Bibhavu3.Mamzon.R
import com.Bibhavu3.Mamzon.domain.model.Decoration
import com.Bibhavu3.Mamzon.domain.model.PlantSpecies
import com.Bibhavu3.Mamzon.domain.model.ZoneId

data class SpeciesArt(
    @DrawableRes val image: Int,
    @StringRes val title: Int,
    @StringRes val description: Int
)

fun speciesArt(species: PlantSpecies): SpeciesArt = when (species) {
    PlantSpecies.GOLDEN_ORCHID -> SpeciesArt(
        image = R.drawable.sprite_1,
        title = R.string.species_golden_orchid,
        description = R.string.species_golden_orchid_note
    )
    PlantSpecies.GIANT_FERN -> SpeciesArt(
        image = R.drawable.sprite_2,
        title = R.string.species_giant_fern,
        description = R.string.species_giant_fern_note
    )
    PlantSpecies.CACAO_SAPLING -> SpeciesArt(
        image = R.drawable.sprite_3,
        title = R.string.species_cacao_sapling,
        description = R.string.species_cacao_sapling_note
    )
    PlantSpecies.BLUE_MORPHO -> SpeciesArt(
        image = R.drawable.sprite_4,
        title = R.string.species_blue_morpho,
        description = R.string.species_blue_morpho_note
    )
    PlantSpecies.SEED_POUCH -> SpeciesArt(
        image = R.drawable.sprite_5,
        title = R.string.species_seed_pouch,
        description = R.string.species_seed_pouch_note
    )
}

@StringRes
fun zoneTitle(zone: ZoneId): Int = when (zone) {
    ZoneId.MIST_BASIN -> R.string.zone_mist_basin
    ZoneId.FERN_HOLLOW -> R.string.zone_fern_hollow
    ZoneId.CANOPY_HEIGHTS -> R.string.zone_canopy_heights
    ZoneId.DAILY -> R.string.zone_daily
}

@StringRes
fun decorationTitle(decoration: Decoration): Int = when (decoration.id) {
    "stone_path" -> R.string.decoration_stone_path
    "lantern_row" -> R.string.decoration_lantern_row
    else -> R.string.decoration_flower_arch
}
