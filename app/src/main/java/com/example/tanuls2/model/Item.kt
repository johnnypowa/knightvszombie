package com.example.tanuls2.model

open class Item(var itemName:String?,
                var level:Int,
                var experience:Int,
                var maxHealth:Int,
                var damage:Int,
                var criticalHitChance: Float,
                var blockChance: Float,
                var lifeSteal:Float,
                var price:Int = 0,
                var type:ItemType){
}

enum class ItemType {
    WEAPON, ARMOR, JEWELLERY, POTION,EMPTY_SLOT
}