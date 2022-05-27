package com.example.tanuls2.service.datasource

import com.example.tanuls2.handler.SharedPreferencesHandler
import com.example.tanuls2.model.Item


class InventoryLocalDataSource() {

    fun getItemListFromLocal() : ArrayList<Item> {
        return SharedPreferencesHandler.storedItemList
    }

    fun saveItemListToLocal(items: ArrayList<Item>) {
        SharedPreferencesHandler.storedItemList = items
    }

}