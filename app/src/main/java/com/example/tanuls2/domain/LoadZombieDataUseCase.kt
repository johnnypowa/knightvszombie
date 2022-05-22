package com.example.tanuls2.domain

import com.example.tanuls2.model.Zombie
import com.example.tanuls2.service.repository.CombatRepository
import io.reactivex.rxjava3.core.Single

class LoadZombieDataUseCase(val combatRepository: CombatRepository) {

    fun execute() : Single<Zombie> {
        return combatRepository.fetchZombieData()
    }
}