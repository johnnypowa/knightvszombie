package com.example.tanuls2.service.model.response

import com.google.gson.annotations.SerializedName

data class CardResponseModel(
    @SerializedName("code") val cardCode: String,
    @SerializedName("image") val cardImage:String,
    @SerializedName("images") val cardImages:CardImagesResponseModel,
    @SerializedName("value") val cardValue:String,
    @SerializedName("suit") val cardType:String
)

//    {
//        "code": "7D",
//        "image": "https://deckofcardsapi.com/static/img/7D.png",
//        "images": {
//            "svg": "https://deckofcardsapi.com/static/img/7D.svg",
//            "png": "https://deckofcardsapi.com/static/img/7D.png"
//        },
//        "value": "7",
//        "suit": "DIAMONDS"
//    }
