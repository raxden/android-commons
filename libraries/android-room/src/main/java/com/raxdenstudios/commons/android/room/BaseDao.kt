package com.raxdenstudios.commons.android.room

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert

interface BaseDao<in T> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIgnore(type: T): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIgnore(type: List<T>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(type: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(types: List<T>): List<Long>

    @Update
    suspend fun update(type: T): Int

    @Update
    suspend fun update(type: List<T>): Int

    @Delete
    suspend fun delete(type: T): Int

    @Delete
    suspend fun delete(type: List<T>): Int

    @Upsert
    suspend fun upsert(types: List<T>)

    @Transaction
    suspend fun upsert(type: T) {
        val id: Long = insert(type)
        if (id == -1L) update(type)
    }
}
