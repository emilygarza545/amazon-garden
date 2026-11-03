package com.Bibhavu3.Mamzon.domain.usecase

import com.Bibhavu3.Mamzon.domain.model.Tile

class FindHintUseCase {

    operator fun invoke(current: List<Tile>, solved: List<Tile>): Int {
        for (i in current.indices) {
            if (!current[i].rotatable) continue
            if (current[i].effectiveEdges != solved[i].effectiveEdges) return i
        }
        return -1
    }
}
