package com.example.tanuls2.ui.viewmodel


import android.view.View
import androidx.lifecycle.ViewModel
import com.example.tanuls2.domain.LoadKnightDataUseCase
import com.example.tanuls2.domain.SaveKnightToDbUseCase
import com.example.tanuls2.model.Item
import com.example.tanuls2.model.Knight
import com.example.tanuls2.util.OnceLiveEvent
import com.example.tanuls2.util.SingleEvent
import com.example.tanuls2.util.plusAssign
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class InventoryViewModel(private val loadKnightDataUseCase: LoadKnightDataUseCase,
                         private val saveKnightToDbUseCase: SaveKnightToDbUseCase) : ViewModel() {

    val onceLiveEvent = OnceLiveEvent<SingleEvent>() //Live data

    var currentItems: ArrayList<Item> = arrayListOf()

    var currentKnight: Knight? = null

    val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun onItemClicked(item: Item, view: View) {
        onceLiveEvent.postValue(ItemClick(item, view))
    }

    fun loadAllInventory() {
        compositeDisposable += loadKnightDataUseCase.execute()
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(Schedulers.io())
            .subscribe(
                { knightEntityData ->
                    currentKnight = Knight(knightEntityData.uid, knightEntityData.experience,
                        knightEntityData.currentHealth, knightEntityData.maxHealth,
                        knightEntityData.level, knightEntityData.damage,
                        knightEntityData.criticalHitChance, knightEntityData.blockChance,
                        knightEntityData.itemList)
                    currentItems = currentKnight!!.itemList
                    onceLiveEvent.postValue(LoadInventory(currentItems))
                },{
                    onceLiveEvent.postValue(ShowError(it.message))
                }
            )
    }

    fun saveKnightToDb() {

        saveKnightToDbUseCase.execute(currentKnight!!)
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(Schedulers.io())
            .delay(500L, TimeUnit.MILLISECONDS)
            .subscribe({
                onceLiveEvent.postValue(SuccessfulSave)
            },{
            })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}

class ItemClick(val item: Item, val view: View) : SingleEvent
class LoadInventory(val loadItemList: ArrayList<Item>) : SingleEvent