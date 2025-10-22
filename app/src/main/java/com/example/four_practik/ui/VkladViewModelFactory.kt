package com.example.four_practik.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.four_practik.data.VkladRepository

class VkladViewModelFactory(
    private val repository: VkladRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VkladViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VkladViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}