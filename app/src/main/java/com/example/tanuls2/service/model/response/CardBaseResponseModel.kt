package com.example.tanuls2.service.model.response

import com.google.gson.annotations.SerializedName

data class CardBaseResponseModel(
    @SerializedName("success") val isSuccess: Boolean,
    @SerializedName("deck_id") val deckId: String,
    @SerializedName("cards") val cardList: ArrayList<CardResponseModel> = arrayListOf(),
    @SerializedName("remaining") val remainingCards: Int

)

//{
//    "success": true,
//    "deck_id": "mrpwsca9qvnp",
//    "cards": [
//    {
//        "code": "7D",
//        "image": "https://deckofcardsapi.com/static/img/7D.png",
//        "images": {
//        "svg": "https://deckofcardsapi.com/static/img/7D.svg",
//        "png": "https://deckofcardsapi.com/static/img/7D.png"
//    },
//    "value": "7",
//    "suit": "DIAMONDS"
//    }
//    ],
//    "remaining": 50
//}