package com.example.AuthApp.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.AuthApp.data.AuthDataStore
import com.example.AuthApp.data.AuthRepository
import com.example.AuthApp.data.TokenManager

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class AuthViewModel(application: Application) : AndroidViewModel(application) {
    val dataStore = AuthDataStore(application)
    val repository = AuthRepository()
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            TokenManager.token = dataStore.tokenFlow.first()
            //val token = dataStore.tokenFlow.first()
            //_uiState.value = _uiState.value.copy(token = token)
        }
    }

    fun register(
        login: String,
        email: String,
        password: String,
        phoneNumber: String,
        firstName: String,
        lastName: String,
        patronymic: String,
        dateOfBirth: String,
        gender: String,
        groupId: Int,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = repository.register(
                login = login,
                email = email,
                password = password,
                phoneNumber = phoneNumber,
                firstName = firstName,
                lastName = lastName,
                patronymic = patronymic,
                dateOfBirth = dateOfBirth,
                gender = gender,
                groupId = groupId
            )
            authHandler(result, onSuccess)
        }
    }

    fun login(login: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = repository.login(login, password)
            authHandler(result, onSuccess)
        }
    }

    fun logout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            dataStore.clearToken()
            _uiState.value = _uiState.value.copy(token = null, users = emptyList())
            onLoggedOut()
        }
    }

    fun loadGroups() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = repository.getGroups()
            result.onSuccess { groups ->
                _uiState.value = _uiState.value.copy(isLoading = false, groups = groups)
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = e.message)
            }
        }
    }

    fun loadUsers() {
        viewModelScope.launch {
            val token = TokenManager.token //_uiState.value.token
            if (token == null) {
                _uiState.value = _uiState.value.copy(errorMessage = "No token available")
                return@launch
            }
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = repository.getUsers(token)
            result.onSuccess { users ->
                _uiState.value = _uiState.value.copy(isLoading = false, users = users)
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = e.message)
            }
        }
    }
    private fun authHandler(result: Result<String>,
                                    onSuccess: () -> Unit){
        result.onSuccess { token ->
            viewModelScope.launch { dataStore.saveToken(token) }
            TokenManager.token = token
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                //token = token,
                errorMessage = null
            )
            onSuccess()
        }.onFailure { e ->
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                errorMessage = e.message
                //token = null
            )
                TokenManager.token = null

        }
    }
}


