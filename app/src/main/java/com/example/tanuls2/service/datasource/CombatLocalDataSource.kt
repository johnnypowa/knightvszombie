package com.example.tanuls2.service.datasource

import com.example.tanuls2.db.dao.KnightDao
import com.example.tanuls2.db.entity.KnightEntity
import com.example.tanuls2.handler.SharedPreferencesHandler
import com.example.tanuls2.model.EmptySlot
import com.example.tanuls2.model.Item
import com.example.tanuls2.model.Knight
import com.example.tanuls2.model.Zombie
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class CombatLocalDataSource(val knightDao: KnightDao) {

    fun fetchLocalKnightData() : Single<KnightEntity> {
        return Single.fromCallable {
            if(SharedPreferencesHandler.isFirstStart()){
                var itemList = arrayListOf<Item>()
                for (i in 0..11) {
                    itemList.add(EmptySlot())
                }
                knightDao.insertKnight(knightEntity = KnightEntity(0,0,100,100,1,100,0.2f,0.2f, itemList))
                SharedPreferencesHandler.setFirstStart(false)
            }
            knightDao.getKnight()
        }.subscribeOn(Schedulers.io())
    }

    fun fetchLocalZombieData() : Single<Zombie> {
        SharedPreferencesHandler.storedZombie = SharedPreferencesHandler.storedZombie ?: Zombie()
        return Single.fromCallable { SharedPreferencesHandler.storedZombie!! }
            .subscribeOn(Schedulers.io())
        //return SharedPreferencesHandler.storedZombie!!
    }

    fun saveKnightToDb(knight: Knight) : Completable {
        return Completable.fromCallable {
            knightDao.insertKnight(KnightEntity(0, knight.experience, knight.currentHealth, knight.maxHealth, knight.level, knight.damage, knight.criticalHitChance, knight.blockChance, knight.itemList))
        }.subscribeOn(Schedulers.io())
    }
}