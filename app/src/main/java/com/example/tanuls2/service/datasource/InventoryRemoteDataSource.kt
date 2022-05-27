package com.example.tanuls2.service.datasource

import com.example.tanuls2.service.model.response.CardBaseResponseModel
import io.reactivex.rxjava3.core.Single

class InventoryRemoteDataSource(val apiService: ApiService) {

    fun drawCard() : Single<CardBaseResponseModel> {
        return apiService.drawCard(1)
    }

}