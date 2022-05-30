package com.example.tanuls2.model

import com.example.tanuls2.handler.SharedPreferencesHandler

data class Knight(var itemList: ArrayList<Item> = ArrayList(12)) : Character(0,
                                                            100,
                                                            100,
                                                            1,
                                                            100,
                                                            0.2f,
                                                            0.2f) {
    init {

    }
}
