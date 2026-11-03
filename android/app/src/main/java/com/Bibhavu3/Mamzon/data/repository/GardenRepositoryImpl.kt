package com.Bibhavu3.Mamzon.data.repository

import com.Bibhavu3.Mamzon.data.local.GardenPreferences
import com.Bibhavu3.Mamzon.domain.model.GardenProgress
import com.Bibhavu3.Mamzon.domain.repository.GardenRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GardenRepositoryImpl(
    private val preferences: GardenPreferences
) : GardenRepository {

    private val state = MutableStateFlow(preferences.read())

    override val progress: StateFlow<GardenProgress> = state.asStateFlow()

    override fun update(transform: (GardenProgress) -> GardenProgress) {
        val next = transform(state.value)
        state.value = next
        preferences.write(next)
    }

    override fun clear() {
        preferences.wipe()
        state.value = GardenProgress()
    }
}
