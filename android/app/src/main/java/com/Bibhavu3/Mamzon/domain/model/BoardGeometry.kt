package com.Bibhavu3.Mamzon.domain.model

object BoardGeometry {

    fun neighbour(index: Int, edge: Edge, cols: Int, rows: Int): Int? {
        var row = index / cols
        var col = index % cols
        when (edge) {
            Edge.TOP -> row -= 1
            Edge.RIGHT -> col += 1
            Edge.BOTTOM -> row += 1
            Edge.LEFT -> col -= 1
        }
        if (row < 0 || row >= rows || col < 0 || col >= cols) return null
        return row * cols + col
    }

    fun layersMatch(first: Tile, second: Tile): Boolean =
        first.kind == TileKind.RISER || second.kind == TileKind.RISER || first.layer == second.layer

    fun joined(from: Tile, edge: Edge, to: Tile): Boolean =
        edge in from.effectiveEdges && edge.opposite() in to.effectiveEdges && layersMatch(from, to)
}
