package com.example.four_practik.ui

import androidx.lifecycle.ViewModel
import com.example.four_practik.model.VkladUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class VkladViewModel: ViewModel (){
    private val _uiState = MutableStateFlow(VkladUiState())

    val uiState: StateFlow<VkladUiState> = _uiState.asStateFlow()

    fun uodateStartSumm (value: String){
        val startSumm = value.toDoubleOrNull() ?: 0.0
        _uiState.update { currentState ->
            val newSummary = (startSumm + _uiState.value.everyMounthPay * _uiState.value.period) + (startSumm + _uiState.value.everyMounthPay * _uiState.value.period) * ((_uiState.value.procient / 100) / 12 * _uiState.value.period)
            currentState.copy(
                summary = newSummary,
                startSumm = startSumm
            )
        }

    }

    fun updateProcient (value: String) {
        val procient = value.toDoubleOrNull() ?: 0.0
        _uiState.update { currentState ->
            val newSummary = (_uiState.value.startSumm + _uiState.value.everyMounthPay * _uiState.value.period) + (_uiState.value.startSumm + _uiState.value.everyMounthPay * _uiState.value.period) * ((procient / 100) / 12 * _uiState.value.period)
            currentState.copy(
                summary = newSummary,
                procient = procient
            )
        }
    }

    fun updateEveryMounthPay (value: String) {
        val everyMounthPay = value.toDoubleOrNull() ?: 0.0
        _uiState.update { currentState ->
            val newSummary = (_uiState.value.startSumm + everyMounthPay * _uiState.value.period) + (_uiState.value.startSumm + everyMounthPay * _uiState.value.period) * ((_uiState.value.procient / 100) / 12 * _uiState.value.period)
            currentState.copy(
                summary = newSummary,
                everyMounthPay = everyMounthPay
            )
        }
    }

    fun updatePeriod (value: String) {
        val period = value.toDoubleOrNull() ?: 0.0
        _uiState.update { currentState ->
            val newSummary = (_uiState.value.startSumm + _uiState.value.everyMounthPay * period) + (_uiState.value.startSumm + _uiState.value.everyMounthPay * period) * ((_uiState.value.procient/ 100) / 12 * period)
            currentState.copy(
                summary = newSummary,
                period = period
            )
        }
    }

    fun resetVklad () {
        _uiState.value = VkladUiState()
    }

}