package com.example.tanuls2.service.repository

import com.example.tanuls2.db.entity.KnightEntity
import com.example.tanuls2.model.Knight
import com.example.tanuls2.model.Zombie
import com.example.tanuls2.service.datasource.CombatLocalDataSource
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class CombatRepository(val combatLocalDataSource: CombatLocalDataSource) {

    //class CombatRepository(val localCombatDataSource: LocalCombatDataSource) : KoinComponent {
        //val localCombatDataSource: LocalCombatDataSource by inject()

    fun fetchKnightData() : Single<KnightEntity> {
        return combatLocalDataSource.fetchLocalKnightData()
    }

    fun fetchZombieData() : Single<Zombie>{
        return combatLocalDataSource.fetchLocalZombieData()
    }

    fun saveKnightToDb(knight: Knight) : Completable {
        return combatLocalDataSource.saveKnightToDb(knight)
    }
}