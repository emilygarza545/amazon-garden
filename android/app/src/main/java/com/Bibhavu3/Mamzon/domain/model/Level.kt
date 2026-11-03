package com.Bibhavu3.Mamzon.domain.model

data class Level(
    val id: Int,
    val zone: ZoneId,
    val solvedTiles: List<Tile>,
    val scramble: List<ScrambleStep>
) {
    val startTiles: List<Tile> = solvedTiles.map { tile ->
        val step = scramble.firstOrNull { it.index == tile.index }
        if (step == null) tile else tile.copy(rotation = (tile.rotation + step.turns) % 4)
    }

    val minimumTaps: Int = run {
        var total = 0
        for (i in solvedTiles.indices) {
            val target = solvedTiles[i].effectiveEdges
            var probe = startTiles[i]
            var taps = 0
            while (taps < 4 && probe.effectiveEdges != target) {
                probe = probe.rotated()
                taps += 1
            }
            total += taps
        }
        total
    }

    val movesAllowed: Int = minimumTaps + 4

    val plantCount: Int = solvedTiles.count { it.kind == TileKind.PLANT }
}
