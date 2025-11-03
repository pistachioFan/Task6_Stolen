package com.example.four_practik.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.four_practik.data.AuthDataStore
import com.example.four_practik.data.AuthRepository
import com.example.four_practik.data.remote.models.UserDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val token: String? = null,
    val users: List<UserDto> = emptyList()
)

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AuthRepository()
    private val dataStore = AuthDataStore(application)

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val token = dataStore.tokenFlow.first()
            _uiState.value = _uiState.value.copy(token = token)
        }
    }

    fun register(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = repository.register(email, password)
            result.onSuccess { token ->
                viewModelScope.launch { dataStore.saveToken(token) }
                _uiState.value = _uiState.value.copy(isLoading = false, token = token)
                onSuccess()
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = e.message)
            }
        }
    }

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = repository.login(email, password)
            result.onSuccess { token ->
                viewModelScope.launch { dataStore.saveToken(token) }
                _uiState.value = _uiState.value.copy(isLoading = false, token = token)
                onSuccess()
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = e.message)
            }
        }
    }

    fun logout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            dataStore.clearToken()
            _uiState.value = _uiState.value.copy(token = null, users = emptyList())
            onLoggedOut()
        }
    }

    fun loadUsers() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = repository.getUsers()
            result.onSuccess { users ->
                _uiState.value = _uiState.value.copy(isLoading = false, users = users)
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = e.message)
            }
        }
    }
}


