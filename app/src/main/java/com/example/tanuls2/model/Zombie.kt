package com.example.tanuls2.model

data class Zombie(
    val experience: Int = 0,
    var currentHealth: Int = 1000,
    val maxHealth: Int = 1000,
    val level:Int = 1,
    val damage:Int = 10,
    val criticalHitChance:Float = 0.2f,
    var blockChance:Float = 0.3f
)