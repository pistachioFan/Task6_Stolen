package com.example.four_practik.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.four_practik.data.VkladRepository
import com.example.four_practik.data.Vklads
import com.example.four_practik.model.VkladUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class VkladViewModel(private val VkladRepository: VkladRepository): ViewModel (){
    private val _uiState = MutableStateFlow(VkladUiState())
    data class ListUiState(val vkladList: List<Vklads> = listOf())
    val uiState: StateFlow<VkladUiState> = _uiState.asStateFlow()

    suspend fun saveVklad(){
        VkladRepository.insert(_uiState.value.toVklads())
    }

    val listUiState: StateFlow<ListUiState> =
        VkladRepository.getAllItems().map { ListUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = ListUiState()
            )

    fun VkladUiState.toVklads() = Vklads(
        id = 0,
        startSumm = startSumm,
        procient = procient,
        everyMounthPay = everyMounthPay,
        period = period,
    )

    fun uodateStartSumm (value: String){
        val startSumm = value.toDoubleOrNull() ?: 0.0
        _uiState.update { currentState ->
            val newSummary = (startSumm + _uiState.value.everyMounthPay * _uiState.value.period) + (startSumm + _uiState.value.everyMounthPay * _uiState.value.period) * ((_uiState.value.procient / 100) / 12 * _uiState.value.period)
            currentState.copy(
                summary = newSummary.toInt(),
                startSumm = startSumm
            )
        }

    }

    fun updateProcient (value: String) {
        val procient = value.toDoubleOrNull() ?: 0.0
        _uiState.update { currentState ->
            val newSummary = (_uiState.value.startSumm + _uiState.value.everyMounthPay * _uiState.value.period) + (_uiState.value.startSumm + _uiState.value.everyMounthPay * _uiState.value.period) * ((procient / 100) / 12 * _uiState.value.period)
            currentState.copy(
                summary = newSummary.toInt(),
                procient = procient
            )
        }
    }

    fun updateEveryMounthPay (value: String) {
        val everyMounthPay = value.toDoubleOrNull() ?: 0.0
        _uiState.update { currentState ->
            val newSummary = (_uiState.value.startSumm + everyMounthPay * _uiState.value.period) + (_uiState.value.startSumm + everyMounthPay * _uiState.value.period) * ((_uiState.value.procient / 100) / 12 * _uiState.value.period)
            currentState.copy(
                summary = newSummary.toInt(),
                everyMounthPay = everyMounthPay
            )
        }
    }

    fun updatePeriod (value: String) {
        val period = value.toIntOrNull() ?: 0
        _uiState.update { currentState ->
            val newSummary = (_uiState.value.startSumm + _uiState.value.everyMounthPay * period) + (_uiState.value.startSumm + _uiState.value.everyMounthPay * period) * ((_uiState.value.procient/ 100) / 12 * period)
            currentState.copy(
                summary = newSummary.toInt(),
                period = period
            )
        }
    }

    fun resetVklad () {
        _uiState.value = VkladUiState()
    }

}