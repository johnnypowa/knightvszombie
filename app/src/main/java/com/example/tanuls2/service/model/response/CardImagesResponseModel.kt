package com.example.tanuls2.service.model.response

import com.google.gson.annotations.SerializedName

data class CardImagesResponseModel(
    @SerializedName("svg") val cardImageExtensionSvg:String,
    @SerializedName("png") val cardImageExtensionPng:String
)

//            "svg": "https://deckofcardsapi.com/static/img/7D.svg",
//            "png": "https://deckofcardsapi.com/static/img/7D.png"