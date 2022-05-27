package com.example.tanuls2.service.repository

import com.example.tanuls2.model.*
import com.example.tanuls2.service.datasource.InventoryLocalDataSource
import com.example.tanuls2.service.datasource.InventoryRemoteDataSource
import io.reactivex.rxjava3.core.Single

class InventoryRepository(val inventoryLocalDatasource:InventoryLocalDataSource, val inventoryRemoteDataSource:InventoryRemoteDataSource) {

    fun fetchInventory() : Single<ArrayList<Item>> {
        //Todo: kiszedem lokalbol az item listet és tovabb passzolom a show invenotry usecase-nek
        return Single.just(arrayListOf<Item>())
    }

    fun generateItemFromRemote() : Single<Item> {
        //TODO: itt fogom a remote card alapján legenerálni az item-et, majd továbbitom az uj item-et a combat fragment felé
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