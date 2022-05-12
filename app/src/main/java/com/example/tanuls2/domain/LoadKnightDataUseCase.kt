package com.example.tanuls2.domain

import com.example.tanuls2.model.Knight
import com.example.tanuls2.service.repository.CombatRepository

class LoadKnightDataUseCase(val combatRepository: CombatRepository) {

    fun execute() : Knight {
        return combatRepository.fetchKnightData()
    }
}