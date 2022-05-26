package com.example.tanuls2.service.repository

import com.example.tanuls2.service.datasource.InventoryLocalDataSource
import com.example.tanuls2.service.datasource.InventoryRemoteDataSource

class InventoryRepository(val inventoryLocalDatasource:InventoryLocalDataSource, val inventoryRemoteDataSource:InventoryRemoteDataSource) {

}