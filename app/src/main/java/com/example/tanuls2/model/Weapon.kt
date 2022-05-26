package com.example.tanuls2.model

class Weapon(itemName: String, level: Int = 0, experience: Int = 0, maxHealth: Int = 100, damage: Int = 10,
             crit: Float = 1f, block: Float = 1f, lifeSteal: Float = 1f, price: Int = 1000) : Item(itemName,
                    level,
                    experience,
                    maxHealth,
                    damage,
                    crit,
                    block,
                    lifeSteal,
                    price, ItemType.WEAPON)
