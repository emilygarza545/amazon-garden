package com.Bibhavu3.Mamzon.domain.usecase

class AwardSeedsUseCase(
    private val perPlant: Int,
    private val perSpareMove: Int,
    private val clearBonus: Int
) {

    operator fun invoke(hydrated: Int, spareMoves: Int, restored: Boolean): Int {
        var seeds = hydrated * perPlant
        if (restored) {
            seeds += spareMoves * perSpareMove
            seeds += clearBonus
        }
        return seeds
    }
}
