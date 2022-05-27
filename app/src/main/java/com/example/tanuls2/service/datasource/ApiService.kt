package com.example.tanuls2.service.datasource

import com.example.tanuls2.service.model.response.CardBaseResponseModel
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("new/draw/")
    fun drawCard(@Query("count", encoded = true) count: Int) : Single<CardBaseResponseModel>
}