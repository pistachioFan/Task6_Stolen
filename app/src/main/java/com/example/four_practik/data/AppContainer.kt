package com.example.four_practik.data

import android.content.Context

interface AppContainer {
    val VkladRepository: VkladRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val VkladRepository: VkladRepository by lazy {
        OfflineVkladRepository(VkladsDatabase.getDatabase(context).vkladDao())
    }
}