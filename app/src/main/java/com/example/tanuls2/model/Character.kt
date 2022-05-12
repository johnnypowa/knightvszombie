package com.example.tanuls2.model

open class Character (var experience:Int,
                      var currentHealth: Int,
                      var maxHealth:Int,
                      var level:Int,
                      var damage:Int,
                      var criticalHitChance: Float,
                      var blockChance: Float,
                      var items: ArrayList<Item> = arrayListOf()) {

}