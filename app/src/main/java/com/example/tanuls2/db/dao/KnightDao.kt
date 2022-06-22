package com.example.tanuls2.db.dao

import androidx.room.*
import com.example.tanuls2.db.entity.KnightEntity

@Dao
interface KnightDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertKnight(knightEntity: KnightEntity)

    @Query("SELECT * FROM knight_table")
    fun getKnight(): KnightEntity

    @Update
    fun update(knightEntity: KnightEntity)

//        @Query("UPDATE knight_table SET currentHealth= :currentHealth")
//        fun updateCurrentHealth(currentHealth: Int)

        //@Query("")

//        @Query("SELECT * FROM user WHERE uid IN (:userIds)")
//        fun loadAllByIds(userIds: IntArray): List<User>
//
//        @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
//                "last_name LIKE :last LIMIT 1")
//        fun findByName(first: String, last: String): User
//
//        @Insert
//        fun insertAll(vararg users: User)
//
//        @Delete
//        fun delete(user: User)
    }

