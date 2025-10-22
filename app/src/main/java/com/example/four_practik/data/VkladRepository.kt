package com.example.four_practik.data

import kotlinx.coroutines.flow.Flow

interface VkladRepository {
    fun getAllItems(): Flow<List<Vklads>>
    suspend fun insert(vklad: Vklads)
}