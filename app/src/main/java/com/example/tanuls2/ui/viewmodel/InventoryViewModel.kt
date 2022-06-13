package com.example.tanuls2.ui.viewmodel


import android.view.View
import androidx.lifecycle.ViewModel
import com.example.tanuls2.domain.InventoryDataUseCase
import com.example.tanuls2.model.Item
import com.example.tanuls2.util.OnceLiveEvent
import com.example.tanuls2.util.SingleEvent
import com.example.tanuls2.util.plusAssign
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class InventoryViewModel(private val inventoryDataUseCase: InventoryDataUseCase) : ViewModel() {

    val onceLiveEvent = OnceLiveEvent<SingleEvent>() //Live data

    var currentItems: ArrayList<Item> = arrayListOf()

    val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun onItemLongClicked(item: Item, view: View) {
        onceLiveEvent.postValue(ItemClick(item, view))
    }

    fun loadAllInventory() {
        compositeDisposable += inventoryDataUseCase.execute()
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(Schedulers.io())
            .subscribe(
                {
                    currentItems = it
                    onceLiveEvent.postValue(LoadInventory(it))
                },{
                    onceLiveEvent.postValue(ShowError(it.message))
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}

class ItemClick(val item: Item, val view: View) : SingleEvent
class LoadInventory(val loadItemList: ArrayList<Item>) : SingleEvent