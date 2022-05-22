package com.example.tanuls2.service.datasource

import com.example.tanuls2.handler.SharedPreferencesHandler
import com.example.tanuls2.model.Knight
import com.example.tanuls2.model.Zombie
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class CombatLocalDataSource() {

    fun fetchLocalKnightData() : Single<Knight> {
        SharedPreferencesHandler.storedKnight = SharedPreferencesHandler.storedKnight ?: Knight()
        return Single.fromCallable { SharedPreferencesHandler.storedKnight!! }
            .subscribeOn(Schedulers.io())
    }

    fun fetchLocalZombieData() : Single<Zombie> {
        SharedPreferencesHandler.storedZombie = SharedPreferencesHandler.storedZombie ?: Zombie()
        return Single.fromCallable { SharedPreferencesHandler.storedZombie!! }
            .subscribeOn(Schedulers.io())
        //return SharedPreferencesHandler.storedZombie!!
    }
}