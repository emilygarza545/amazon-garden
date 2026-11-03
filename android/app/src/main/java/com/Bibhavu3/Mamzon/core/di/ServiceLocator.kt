package com.Bibhavu3.Mamzon.core.di

import android.content.Context
import com.Bibhavu3.Mamzon.core.config.GameConfig
import com.Bibhavu3.Mamzon.data.local.GardenPreferences
import com.Bibhavu3.Mamzon.data.repository.GardenRepositoryImpl
import com.Bibhavu3.Mamzon.domain.repository.GardenRepository
import com.Bibhavu3.Mamzon.domain.usecase.AwardSeedsUseCase
import com.Bibhavu3.Mamzon.domain.usecase.ComputeFlowUseCase
import com.Bibhavu3.Mamzon.domain.usecase.EvaluateRoundUseCase
import com.Bibhavu3.Mamzon.domain.usecase.FindHintUseCase

object ServiceLocator {

    private var repository: GardenRepository? = null

    val computeFlow: ComputeFlowUseCase = ComputeFlowUseCase()

    val evaluateRound: EvaluateRoundUseCase = EvaluateRoundUseCase()

    val findHint: FindHintUseCase = FindHintUseCase()

    val awardSeeds: AwardSeedsUseCase = AwardSeedsUseCase(
        perPlant = GameConfig.SEEDS_PER_PLANT,
        perSpareMove = GameConfig.SEEDS_PER_SPARE_MOVE,
        clearBonus = GameConfig.LEVEL_CLEAR_BONUS
    )

    fun init(context: Context) {
        if (repository == null) {
            repository = GardenRepositoryImpl(GardenPreferences(context.applicationContext))
        }
    }

    val gardenRepository: GardenRepository
        get() = requireNotNull(repository) { "ServiceLocator.init was not called" }
}
