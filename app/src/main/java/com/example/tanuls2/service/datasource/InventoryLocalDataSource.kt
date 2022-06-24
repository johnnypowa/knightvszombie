package com.example.tanuls2.service.datasource

import com.example.tanuls2.handler.SharedPreferencesHandler
import com.example.tanuls2.model.Item
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers


class InventoryLocalDataSource() {

    fun getItemListFromLocal() : Single<ArrayList<Item>> {
        return Single.fromCallable { SharedPreferencesHandler.storedItemList }
            .subscribeOn(Schedulers.io())
    }
}