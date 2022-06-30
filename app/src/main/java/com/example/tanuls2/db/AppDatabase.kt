package com.example.tanuls2.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.tanuls2.db.converter.DataConverter
import com.example.tanuls2.db.dao.KnightDao
import com.example.tanuls2.db.entity.KnightEntity

@Database(entities = [KnightEntity::class], version = 2)
@TypeConverters(DataConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun knightDao(): KnightDao
}