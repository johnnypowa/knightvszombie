package com.example.tanuls2.domain

import com.example.tanuls2.model.Knight
import com.example.tanuls2.service.repository.CombatRepository
import io.reactivex.rxjava3.core.Completable

class SaveKnightToDbUseCase(val combatRepository: CombatRepository) {

    fun execute(knight: Knight) : Completable {
           return  combatRepository.saveKnightToDb(knight)
    }
}