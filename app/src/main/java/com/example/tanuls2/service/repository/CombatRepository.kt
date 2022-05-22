package com.example.tanuls2.service.repository

import com.example.tanuls2.model.Knight
import com.example.tanuls2.model.Zombie
import com.example.tanuls2.service.datasource.CombatLocalDataSource
import io.reactivex.rxjava3.core.Single

class CombatRepository(val combatLocalDataSource: CombatLocalDataSource) {

    //class CombatRepository(val localCombatDataSource: LocalCombatDataSource) : KoinComponent {
        //val localCombatDataSource: LocalCombatDataSource by inject()

    fun fetchKnightData() : Single<Knight> {
        return combatLocalDataSource.fetchLocalKnightData()
    }

    fun fetchZombieData() : Single<Zombie>{
        return combatLocalDataSource.fetchLocalZombieData()
    }
}