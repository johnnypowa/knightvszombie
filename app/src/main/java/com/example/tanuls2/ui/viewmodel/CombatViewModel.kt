package com.example.tanuls2.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tanuls2.db.entity.KnightEntity
import com.example.tanuls2.domain.DroppedItemUseCase
import com.example.tanuls2.domain.LoadKnightDataUseCase
import com.example.tanuls2.domain.LoadZombieDataUseCase
import com.example.tanuls2.domain.SaveKnightToDbUseCase
import com.example.tanuls2.handler.SharedPreferencesHandler
import com.example.tanuls2.model.CombatModel
import com.example.tanuls2.model.Item
import com.example.tanuls2.model.Knight
import com.example.tanuls2.model.Zombie
import com.example.tanuls2.util.OnceLiveEvent
import com.example.tanuls2.util.SingleEvent
import com.example.tanuls2.util.plusAssign
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

//MVVM architektúra (Model - View - ViewModel)
class CombatViewModel(
    private val loadKnightDataUseCase: LoadKnightDataUseCase,
    private val loadZombieDataUseCase: LoadZombieDataUseCase,
    private val droppedItemUseCase: DroppedItemUseCase,
    private val saveKnightToDbUseCase: SaveKnightToDbUseCase
) : ViewModel() {

    val onceLiveEvent = OnceLiveEvent<SingleEvent>() //Live data

    val compositeDisposable: CompositeDisposable = CompositeDisposable()

    val knightDataFlow : Single<KnightEntity> = loadKnightDataUseCase.execute()//.delay(1L, TimeUnit.SECONDS)
    val zombieDataFlow: Single<Zombie> = loadZombieDataUseCase.execute()//.delay(1L, TimeUnit.SECONDS)

    var currentKnight: Knight? = null

    fun loadAllContent() {
        compositeDisposable += Single.zip(knightDataFlow, zombieDataFlow) { knightEntityData, zombieData ->
                CombatModel(Knight(knightEntityData.uid, knightEntityData.experience,
                    knightEntityData.currentHealth, knightEntityData.maxHealth,
                    knightEntityData.level, knightEntityData.damage,
                    knightEntityData.criticalHitChance, knightEntityData.blockChance,
                    knightEntityData.itemList, knightEntityData.skillList), zombieData)
            }
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(Schedulers.io())
            .subscribe(
                {
                    currentKnight = it.knight
                    //it.knight.itemList = SharedPreferencesHandler.storedItemList
                    onceLiveEvent.postValue(ShowAllContent(it.knight, it.zombie))
                },{
                    onceLiveEvent.postValue(ShowError(it.message))
                }
            )
    }

    fun getDroppedItem() {
        compositeDisposable += droppedItemUseCase.execute()
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(Schedulers.io())
            .subscribe(
                {
                    onceLiveEvent.postValue(DroppedItem(it))
                },{
                    onceLiveEvent.postValue(ShowError(it.message))
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun saveKnightToDb() {
        saveKnightToDbUseCase.execute(currentKnight!!)
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(Schedulers.io())
            .subscribe({
                   onceLiveEvent.postValue(SuccessfulSave)
                },{

                }
            )
    }
}

class ShowAllContent(val knightData: Knight, val zombieData: Zombie) : SingleEvent
class ShowError(val errorMessage: String?) : SingleEvent
class DroppedItem(val droppedItem: Item) : SingleEvent
object SuccessfulSave : SingleEvent