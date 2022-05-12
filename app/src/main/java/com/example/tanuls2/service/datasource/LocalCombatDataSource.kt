package com.example.tanuls2.service.datasource

import com.example.tanuls2.handler.SharedPreferencesHandler
import com.example.tanuls2.model.Knight
import com.example.tanuls2.model.Zombie

class LocalCombatDataSource() {

    fun fetchLocalKnightData() : Knight {
        SharedPreferencesHandler.storedKnight = SharedPreferencesHandler.storedKnight ?: Knight()
        return SharedPreferencesHandler.storedKnight!!
    }

    fun fetchLocalZombieData() : Zombie {
        SharedPreferencesHandler.storedZombie = SharedPreferencesHandler.storedZombie ?: Zombie()
        return SharedPreferencesHandler.storedZombie!!
    }
}