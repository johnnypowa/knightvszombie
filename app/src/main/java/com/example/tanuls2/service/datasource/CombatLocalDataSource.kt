package com.example.tanuls2.service.datasource

import com.example.tanuls2.db.dao.KnightDao
import com.example.tanuls2.db.entity.KnightEntity
import com.example.tanuls2.handler.SharedPreferencesHandler
import com.example.tanuls2.model.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class CombatLocalDataSource(val knightDao: KnightDao) {

    companion object {
        const val DOUBLE_HIT = "Dupla ütés"
        const val CRITICAL_HIT = "Kritikus ütés"
        const val PRECISION_HIT  = "Precíz ütés"
        const val LIFE_STEAL_HIT  = "Gyógyító ütés"
        const val HEAL = "Gyógyítás"
    }

    var skillExtraDamage = Skill(DOUBLE_HIT, 1, 0, 100, 0f, 0f, 0f, true, SkillName.DOUBLE_HIT)
    var skillPrecisionHit = Skill(PRECISION_HIT, 1, 0, 0, 0f, 0.0f, 0f, true, SkillName.PRECISION_HIT)
    var skillCriticalHit  = Skill (CRITICAL_HIT, 1, 0, 0, 1.0f, 0f, 0f, true, SkillName.CRITICAL_HIT)
    var skillLifeSteal  = Skill(LIFE_STEAL_HIT, 1, 0, 0, 0f, 0f, 0.5f, true, SkillName.LIFE_STEAL_HIT)
    var skillHeal = Skill(HEAL, 1, 25, 0, 0f, 0f, 0f, false, SkillName.HEAL)
    
    fun fetchLocalKnightData() : Single<KnightEntity> {
        return Single.fromCallable {
            if(SharedPreferencesHandler.isFirstStart()){
                var skillList = arrayListOf(skillExtraDamage, skillCriticalHit, skillPrecisionHit, skillLifeSteal, skillHeal)

                    var itemList = arrayListOf<Item>()
                for (i in 0..11) {
                    itemList.add(EmptySlot())
                }
                knightDao.insertKnight(knightEntity = KnightEntity(0,0,100,100,1,100,0.2f,0.2f, itemList, skillList))
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
            knightDao.insertKnight(KnightEntity(0, knight.experience, knight.currentHealth, knight.maxHealth, knight.level, knight.damage, knight.criticalHitChance, knight.blockChance, knight.itemList, knight.skillList))
        }.subscribeOn(Schedulers.io())
    }


}