package com.example.tanuls2.domain

import com.example.tanuls2.model.Item
import com.example.tanuls2.service.repository.InventoryRepository
import io.reactivex.rxjava3.core.Single

class DroppedItemUseCase(val inventoryRepository: InventoryRepository) {

    fun execute() : Single<Item> {
        return inventoryRepository.generateItemFromRemote()
    }
}