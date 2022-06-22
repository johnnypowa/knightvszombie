package com.example.tanuls2.domain

import com.example.tanuls2.db.entity.KnightEntity
import com.example.tanuls2.model.Knight
import com.example.tanuls2.service.repository.CombatRepository
import io.reactivex.rxjava3.core.Single

class LoadKnightDataUseCase(val combatRepository: CombatRepository) {

    fun execute() : Single<KnightEntity> {
        return combatRepository.fetchKnightData()
    }
}