package com.Bibhavu3.Mamzon.domain.usecase

import com.Bibhavu3.Mamzon.domain.model.BoardGeometry
import com.Bibhavu3.Mamzon.domain.model.Edge
import com.Bibhavu3.Mamzon.domain.model.HydrationReport
import com.Bibhavu3.Mamzon.domain.model.Tile
import com.Bibhavu3.Mamzon.domain.model.TileKind

class EvaluateRoundUseCase {

    operator fun invoke(tiles: List<Tile>, wet: Set<Int>, cols: Int, rows: Int): HydrationReport {
        val hydrated = LinkedHashSet<Int>()
        val drowned = LinkedHashSet<Int>()
        var required = 0
        for (tile in tiles) {
            if (tile.kind != TileKind.PLANT) continue
            required += 1
            val contacts = wetContacts(tiles, tile, wet, cols, rows)
            when {
                contacts > tile.thirst + 1 -> drowned.add(tile.index)
                contacts >= tile.thirst -> hydrated.add(tile.index)
            }
        }
        return HydrationReport(hydrated, drowned, required)
    }

    fun wetContacts(tiles: List<Tile>, plant: Tile, wet: Set<Int>, cols: Int, rows: Int): Int {
        var contacts = 0
        for (edge in Edge.entries) {
            val next = BoardGeometry.neighbour(plant.index, edge, cols, rows) ?: continue
            if (next !in wet) continue
            val source = tiles[next]
            if (!BoardGeometry.layersMatch(plant, source)) continue
            if (edge.opposite() !in source.effectiveEdges) continue
            contacts += 1
        }
        return contacts
    }
}
