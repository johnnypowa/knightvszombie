package com.example.tanuls2.service.repository

import com.example.tanuls2.model.*
import com.example.tanuls2.service.datasource.InventoryLocalDataSource
import com.example.tanuls2.service.datasource.InventoryRemoteDataSource
import io.reactivex.rxjava3.core.Single

class InventoryRepository(val inventoryLocalDatasource:InventoryLocalDataSource, val inventoryRemoteDataSource:InventoryRemoteDataSource) {

    fun fetchInventory() : Single<ArrayList<Item>> {
        return inventoryLocalDatasource.getItemListFromLocal()
    }

    fun generateItemFromRemote() : Single<Item> {
        return inventoryRemoteDataSource.drawCard()
            .map {
                val cardType = it.cardList[0].cardType
                val generatedItem = when (cardType) {
                    "DIAMONDS" -> { Jewellery("Gyűrű") }
                    "HEARTS" -> { Potion() }
                    "CLUBS" -> { Armor("Páncél") }
                    "SPADES" -> { Weapon("Kard") }
                    else -> EmptySlot()
                }
                generatedItem
            }
    }
}