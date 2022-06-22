package com.example.tanuls2.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tanuls2.model.Item

@Entity(tableName = "knight_table")
data class KnightEntity(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "experience") val experience: Int,
    @ColumnInfo(name = "currentHealth") val currentHealth: Int,
    @ColumnInfo(name = "maxHealth") val maxHealth: Int,
    @ColumnInfo(name = "level") val level:Int,
    @ColumnInfo(name = "damage") val damage:Int,
    @ColumnInfo(name ="criticalHitChance") val criticalHitChance:Float,
    @ColumnInfo(name = "blockChance") val blockChance:Float,
    @ColumnInfo(name= "items") val itemList: ArrayList<Item> = arrayListOf())

