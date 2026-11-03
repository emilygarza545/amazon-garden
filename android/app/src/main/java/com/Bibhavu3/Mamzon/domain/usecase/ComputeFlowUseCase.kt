package com.Bibhavu3.Mamzon.domain.usecase

import com.Bibhavu3.Mamzon.domain.model.BoardGeometry
import com.Bibhavu3.Mamzon.domain.model.FlowMap
import com.Bibhavu3.Mamzon.domain.model.Tile
import com.Bibhavu3.Mamzon.domain.model.TileKind

class ComputeFlowUseCase {

    operator fun invoke(tiles: List<Tile>, cols: Int, rows: Int): FlowMap {
        val spring = tiles.indexOfFirst { it.kind == TileKind.SPRING }
        if (spring < 0) return FlowMap(emptySet(), emptyMap())
        val wet = LinkedHashSet<Int>()
        val distance = LinkedHashMap<Int, Int>()
        val queue = ArrayDeque<Int>()
        wet.add(spring)
        distance[spring] = 0
        queue.addLast(spring)
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            val tile = tiles[current]
            for (edge in tile.effectiveEdges) {
                val next = BoardGeometry.neighbour(current, edge, cols, rows) ?: continue
                if (next in wet) continue
                val candidate = tiles[next]
                if (!candidate.carriesWater) continue
                if (!BoardGeometry.joined(tile, edge, candidate)) continue
                wet.add(next)
                distance[next] = (distance[current] ?: 0) + 1
                queue.addLast(next)
            }
        }
        return FlowMap(wet, distance)
    }
}
