package com.example.tanuls2.domain

import com.example.tanuls2.model.Zombie
import com.example.tanuls2.service.repository.CombatRepository

class LoadZombieDataUseCase(val combatRepository: CombatRepository) {

    fun execute() : Zombie {
        return combatRepository.fetchZombieData()
    }
}