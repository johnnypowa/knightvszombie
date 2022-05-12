package com.example.tanuls2.service.repository

import com.example.tanuls2.model.Knight
import com.example.tanuls2.model.Zombie
import com.example.tanuls2.service.datasource.LocalCombatDataSource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CombatRepository(val localCombatDataSource: LocalCombatDataSource) {

    //class CombatRepository(val localCombatDataSource: LocalCombatDataSource) : KoinComponent {

        //val localCombatDataSource: LocalCombatDataSource by inject()

    fun fetchKnightData() : Knight {
        return localCombatDataSource.fetchLocalKnightData()
    }

    fun fetchZombieData() : Zombie {
        return localCombatDataSource.fetchLocalZombieData()
    }
}