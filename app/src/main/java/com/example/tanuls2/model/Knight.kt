package com.example.tanuls2.model

data class Knight(val uid: Int,
                  var experience: Int,
                  var currentHealth: Int,
                  var maxHealth: Int,
                  var level:Int,
                  var damage:Int,
                  var criticalHitChance:Float,
                  var blockChance:Float,
                  var itemList: ArrayList<Item> = arrayListOf())

