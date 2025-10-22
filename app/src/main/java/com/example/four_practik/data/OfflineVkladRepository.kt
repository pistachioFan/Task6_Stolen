package com.example.four_practik.data


import kotlinx.coroutines.flow.Flow

class OfflineVkladRepository(private val vkladDao: VkladDao): VkladRepository {
    override fun getAllItems(): Flow<List<Vklads>> = vkladDao.getAllItems()
    override suspend fun insert(vklad: Vklads) = vkladDao.insert(vklad)
}