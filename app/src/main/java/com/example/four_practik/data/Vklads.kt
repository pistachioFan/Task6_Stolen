package com.example.four_practik.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Vklads")
data class Vklads (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val startSumm: Double = 0.0,
    val procient: Double = 0.0,
    val everyMounthPay: Double = 0.0,
    val period: Int = 0,
    val summary: Int = 0
)