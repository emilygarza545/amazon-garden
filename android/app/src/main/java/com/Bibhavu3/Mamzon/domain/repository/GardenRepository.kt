package com.Bibhavu3.Mamzon.domain.repository

import com.Bibhavu3.Mamzon.domain.model.GardenProgress
import kotlinx.coroutines.flow.StateFlow

interface GardenRepository {

    val progress: StateFlow<GardenProgress>

    fun update(transform: (GardenProgress) -> GardenProgress)

    fun clear()
}
