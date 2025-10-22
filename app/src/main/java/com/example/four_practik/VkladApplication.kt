package com.example.four_practik

import android.app.Application
import com.example.four_practik.data.OfflineVkladRepository
import com.example.four_practik.data.VkladsDatabase
import kotlin.getValue

class VkladApplication : Application() {
    val database by lazy { VkladsDatabase.getDatabase(this) }
    val repository: OfflineVkladRepository by lazy {
        OfflineVkladRepository(database.vkladDao())
    }
}
