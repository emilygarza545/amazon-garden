package com.Bibhavu3.Mamzon.data.sample

import com.Bibhavu3.Mamzon.domain.model.Decoration
import com.Bibhavu3.Mamzon.domain.model.Edge
import com.Bibhavu3.Mamzon.domain.model.FlowLayer
import com.Bibhavu3.Mamzon.domain.model.Level
import com.Bibhavu3.Mamzon.domain.model.PlantSpecies
import com.Bibhavu3.Mamzon.domain.model.ScrambleStep
import com.Bibhavu3.Mamzon.domain.model.Tile
import com.Bibhavu3.Mamzon.domain.model.TileKind
import com.Bibhavu3.Mamzon.domain.model.Zone
import com.Bibhavu3.Mamzon.domain.model.ZoneId

object SampleData {

    const val COLS = 5
    const val ROWS = 6

    val zones: List<Zone> = listOf(
        Zone(ZoneId.MIST_BASIN, listOf(1, 2), 1),
        Zone(ZoneId.FERN_HOLLOW, listOf(3, 4), 3),
        Zone(ZoneId.CANOPY_HEIGHTS, listOf(5, 6), 5),
        Zone(ZoneId.DAILY, listOf(1, 2, 3, 4, 5, 6), 1)
    )

    val atlasSpecies: List<PlantSpecies> = listOf(
        PlantSpecies.GOLDEN_ORCHID,
        PlantSpecies.GIANT_FERN,
        PlantSpecies.CACAO_SAPLING,
        PlantSpecies.BLUE_MORPHO,
        PlantSpecies.SEED_POUCH
    )

    val decorations: List<Decoration> = listOf(
        Decoration("stone_path", 40),
        Decoration("lantern_row", 70),
        Decoration("flower_arch", 120)
    )

    val levels: List<Level> = listOf(
        mistBasinSpringPair(),
        mistBasinTwinBanks(),
        fernHollowDoubleFeed(),
        fernHollowSnakeRun(),
        canopyHeightsFirstRiser(),
        canopyHeightsCrownSpring()
    )

    fun level(id: Int): Level = levels.firstOrNull { it.id == id } ?: levels.first()

    fun dailyLevel(dayOfYear: Int): Level = levels[((dayOfYear % levels.size) + levels.size) % levels.size]

    fun zoneOf(levelId: Int): ZoneId =
        zones.firstOrNull { it.id != ZoneId.DAILY && levelId in it.levelIds }?.id ?: ZoneId.MIST_BASIN

    fun firstLevelOf(zone: ZoneId): Int =
        zones.firstOrNull { it.id == zone }?.levelIds?.firstOrNull() ?: 1

    fun nextLevelId(levelId: Int): Int = if (levelId >= levels.size) 1 else levelId + 1

    fun isZoneUnlocked(zone: ZoneId, highestLevelUnlocked: Int): Boolean {
        if (zone == ZoneId.DAILY) return true
        val required = zones.firstOrNull { it.id == zone }?.unlockedAtLevel ?: 1
        return highestLevelUnlocked >= required
    }

    private fun mistBasinSpringPair(): Level = Level(
        id = 1,
        zone = ZoneId.MIST_BASIN,
        solvedTiles = board(
            plant(6, 1, PlantSpecies.GIANT_FERN),
            channel(11, FlowLayer.GROUND, Edge.TOP, Edge.BOTTOM),
            channel(16, FlowLayer.GROUND, Edge.TOP, Edge.RIGHT),
            spring(17, Edge.LEFT, Edge.BOTTOM),
            channel(22, FlowLayer.GROUND, Edge.TOP, Edge.BOTTOM),
            plant(27, 1, PlantSpecies.CACAO_SAPLING)
        ),
        scramble = listOf(
            ScrambleStep(11, 3),
            ScrambleStep(16, 3),
            ScrambleStep(22, 3)
        )
    )

    private fun mistBasinTwinBanks(): Level = Level(
        id = 2,
        zone = ZoneId.MIST_BASIN,
        solvedTiles = board(
            plant(5, 1, PlantSpecies.GIANT_FERN),
            channel(10, FlowLayer.GROUND, Edge.TOP, Edge.BOTTOM),
            channel(15, FlowLayer.GROUND, Edge.TOP, Edge.RIGHT),
            channel(16, FlowLayer.GROUND, Edge.LEFT, Edge.RIGHT),
            spring(17, Edge.LEFT, Edge.RIGHT),
            channel(18, FlowLayer.GROUND, Edge.LEFT, Edge.RIGHT),
            channel(19, FlowLayer.GROUND, Edge.LEFT, Edge.BOTTOM),
            channel(24, FlowLayer.GROUND, Edge.TOP, Edge.BOTTOM),
            plant(29, 1, PlantSpecies.CACAO_SAPLING)
        ),
        scramble = listOf(
            ScrambleStep(10, 3),
            ScrambleStep(15, 3),
            ScrambleStep(18, 3),
            ScrambleStep(24, 3)
        )
    )

    private fun fernHollowDoubleFeed(): Level = Level(
        id = 3,
        zone = ZoneId.FERN_HOLLOW,
        solvedTiles = board(
            plant(6, 1, PlantSpecies.GIANT_FERN),
            channel(11, FlowLayer.GROUND, Edge.TOP, Edge.BOTTOM, Edge.RIGHT),
            plant(12, 2, PlantSpecies.CACAO_SAPLING),
            channel(13, FlowLayer.GROUND, Edge.BOTTOM, Edge.LEFT),
            rock(14),
            channel(16, FlowLayer.GROUND, Edge.RIGHT, Edge.TOP),
            spring(17, Edge.LEFT, Edge.RIGHT, Edge.BOTTOM),
            channel(18, FlowLayer.GROUND, Edge.LEFT, Edge.TOP),
            channel(22, FlowLayer.GROUND, Edge.TOP, Edge.BOTTOM),
            plant(27, 1, PlantSpecies.GIANT_FERN)
        ),
        scramble = listOf(
            ScrambleStep(11, 3),
            ScrambleStep(13, 3),
            ScrambleStep(16, 3),
            ScrambleStep(18, 3),
            ScrambleStep(22, 3)
        )
    )

    private fun fernHollowSnakeRun(): Level = Level(
        id = 4,
        zone = ZoneId.FERN_HOLLOW,
        solvedTiles = board(
            plant(2, 1, PlantSpecies.GIANT_FERN),
            channel(7, FlowLayer.GROUND, Edge.TOP, Edge.BOTTOM),
            channel(10, FlowLayer.GROUND, Edge.BOTTOM, Edge.RIGHT),
            channel(11, FlowLayer.GROUND, Edge.RIGHT, Edge.BOTTOM, Edge.LEFT),
            channel(12, FlowLayer.GROUND, Edge.TOP, Edge.BOTTOM, Edge.LEFT),
            plant(15, 2, PlantSpecies.CACAO_SAPLING),
            channel(16, FlowLayer.GROUND, Edge.TOP, Edge.LEFT),
            spring(17, Edge.TOP, Edge.RIGHT),
            channel(18, FlowLayer.GROUND, Edge.LEFT, Edge.BOTTOM),
            rock(19),
            channel(23, FlowLayer.GROUND, Edge.TOP, Edge.BOTTOM),
            plant(28, 1, PlantSpecies.GIANT_FERN)
        ),
        scramble = listOf(
            ScrambleStep(7, 3),
            ScrambleStep(10, 3),
            ScrambleStep(11, 3),
            ScrambleStep(12, 3),
            ScrambleStep(16, 3),
            ScrambleStep(18, 3)
        )
    )

    private fun canopyHeightsFirstRiser(): Level = Level(
        id = 5,
        zone = ZoneId.CANOPY_HEIGHTS,
        solvedTiles = board(
            channel(6, FlowLayer.CANOPY, Edge.RIGHT, Edge.BOTTOM),
            channel(7, FlowLayer.CANOPY, Edge.BOTTOM, Edge.LEFT, Edge.RIGHT),
            channel(8, FlowLayer.CANOPY, Edge.LEFT, Edge.BOTTOM),
            rock(10),
            plant(11, 1, PlantSpecies.GOLDEN_ORCHID, FlowLayer.CANOPY),
            riser(12),
            plant(13, 1, PlantSpecies.GIANT_FERN, FlowLayer.CANOPY),
            rock(14),
            channel(16, FlowLayer.GROUND, Edge.RIGHT, Edge.BOTTOM),
            channel(17, FlowLayer.GROUND, Edge.BOTTOM, Edge.LEFT, Edge.TOP),
            plant(21, 2, PlantSpecies.CACAO_SAPLING),
            channel(22, FlowLayer.GROUND, Edge.TOP, Edge.BOTTOM, Edge.RIGHT),
            plant(23, 1, PlantSpecies.GIANT_FERN),
            channel(26, FlowLayer.GROUND, Edge.RIGHT, Edge.TOP),
            spring(27, Edge.TOP, Edge.LEFT)
        ),
        scramble = listOf(
            ScrambleStep(6, 3),
            ScrambleStep(7, 3),
            ScrambleStep(8, 3),
            ScrambleStep(16, 3),
            ScrambleStep(17, 3),
            ScrambleStep(22, 3),
            ScrambleStep(26, 3)
        )
    )

    private fun canopyHeightsCrownSpring(): Level = Level(
        id = 6,
        zone = ZoneId.CANOPY_HEIGHTS,
        solvedTiles = board(
            channel(7, FlowLayer.CANOPY, Edge.BOTTOM, Edge.RIGHT),
            channel(8, FlowLayer.CANOPY, Edge.LEFT, Edge.BOTTOM, Edge.RIGHT),
            channel(9, FlowLayer.CANOPY, Edge.LEFT, Edge.BOTTOM),
            rock(10),
            plant(11, 1, PlantSpecies.GIANT_FERN),
            riser(12),
            plant(13, 1, PlantSpecies.CACAO_SAPLING, FlowLayer.CANOPY),
            plant(14, 1, PlantSpecies.GIANT_FERN, FlowLayer.CANOPY),
            channel(16, FlowLayer.GROUND, Edge.RIGHT, Edge.TOP),
            channel(17, FlowLayer.GROUND, Edge.BOTTOM, Edge.TOP, Edge.LEFT),
            rock(19),
            channel(21, FlowLayer.GROUND, Edge.RIGHT, Edge.BOTTOM),
            spring(22, Edge.TOP, Edge.BOTTOM, Edge.LEFT, Edge.RIGHT),
            channel(23, FlowLayer.GROUND, Edge.LEFT, Edge.BOTTOM),
            channel(26, FlowLayer.GROUND, Edge.TOP, Edge.RIGHT),
            plant(27, 3, PlantSpecies.GOLDEN_ORCHID),
            channel(28, FlowLayer.GROUND, Edge.TOP, Edge.LEFT)
        ),
        scramble = listOf(
            ScrambleStep(7, 3),
            ScrambleStep(8, 3),
            ScrambleStep(16, 3),
            ScrambleStep(17, 3),
            ScrambleStep(21, 3),
            ScrambleStep(23, 3),
            ScrambleStep(26, 3),
            ScrambleStep(28, 3)
        )
    )

    private fun board(vararg entries: Pair<Int, Tile>): List<Tile> {
        val placed = entries.toMap()
        return List(COLS * ROWS) { index ->
            placed[index] ?: Tile(index = index, kind = TileKind.SOIL)
        }
    }

    private fun spring(index: Int, vararg edges: Edge): Pair<Int, Tile> =
        index to Tile(
            index = index,
            kind = TileKind.SPRING,
            layer = FlowLayer.GROUND,
            connections = edges.toSet()
        )

    private fun channel(index: Int, layer: FlowLayer, vararg edges: Edge): Pair<Int, Tile> =
        index to Tile(
            index = index,
            kind = TileKind.CHANNEL,
            layer = layer,
            connections = edges.toSet()
        )

    private fun riser(index: Int): Pair<Int, Tile> =
        index to Tile(
            index = index,
            kind = TileKind.RISER,
            layer = FlowLayer.GROUND,
            connections = setOf(Edge.TOP, Edge.BOTTOM)
        )

    private fun plant(
        index: Int,
        thirst: Int,
        species: PlantSpecies,
        layer: FlowLayer = FlowLayer.GROUND
    ): Pair<Int, Tile> =
        index to Tile(
            index = index,
            kind = TileKind.PLANT,
            layer = layer,
            connections = emptySet(),
            rotation = 0,
            thirst = thirst,
            species = species
        )

    private fun rock(index: Int): Pair<Int, Tile> =
        index to Tile(index = index, kind = TileKind.ROCK, layer = FlowLayer.GROUND)
}
