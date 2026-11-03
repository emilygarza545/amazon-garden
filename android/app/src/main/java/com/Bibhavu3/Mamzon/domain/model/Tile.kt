package com.Bibhavu3.Mamzon.domain.model

data class Tile(
    val index: Int,
    val kind: TileKind,
    val layer: FlowLayer = FlowLayer.GROUND,
    val connections: Set<Edge> = emptySet(),
    val rotation: Int = 0,
    val thirst: Int = 0,
    val species: PlantSpecies? = null
) {
    val rotatable: Boolean
        get() = kind == TileKind.CHANNEL || kind == TileKind.RISER

    val carriesWater: Boolean
        get() = kind == TileKind.SPRING || kind == TileKind.CHANNEL || kind == TileKind.RISER

    val effectiveEdges: Set<Edge>
        get() = connections.mapTo(LinkedHashSet()) { it.turned(rotation) }

    fun rotated(): Tile = copy(rotation = rotation + 1)
}
