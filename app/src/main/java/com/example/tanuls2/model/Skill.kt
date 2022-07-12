package com.example.tanuls2.model

open class Skill(var skillLabel: String?,
                 var level: Int,
                 var health: Int,
                 var damage: Int,
                 var criticalHitChance: Float,
                 var blockChance: Float,
                 var lifeSteal: Float,
                 var equipped: Boolean,
                 var skillName: SkillName)

enum class SkillName {
    DOUBLE_HIT, CRITICAL_HIT, PRECISION_HIT, LIFE_STEAL_HIT, HEAL
}

