package com.example.four_practik.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface VkladDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vklad: Vklads)

    @Query("SELECT * from vklads ORDER BY id DESC")
    fun getAllItems(): Flow<List<Vklads>>
}