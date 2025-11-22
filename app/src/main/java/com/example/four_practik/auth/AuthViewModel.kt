package com.example.four_practik.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.four_practik.data.AuthDataStore
import com.example.four_practik.data.AuthRepository
import com.example.four_practik.data.remote.models.GroupDto
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
    val tokenResponseCode: Int? = null,
    val registrationSuccess: Boolean = false,
    val users: List<UserDto> = emptyList(),
    val groups: List<GroupDto> = emptyList()
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
            result.onSuccess { (token, responseCode) ->
                viewModelScope.launch { dataStore.saveToken(token) }
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    token = token,
                    tokenResponseCode = responseCode,
                    registrationSuccess = true,
                    errorMessage = null
                )
                onSuccess()
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message,
                    registrationSuccess = false
                )
            }
        }
    }

    fun login(login: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = repository.login(login, password)
            result.onSuccess { (token, responseCode) ->
                viewModelScope.launch { dataStore.saveToken(token) }
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    token = token,
                    tokenResponseCode = responseCode,
                    errorMessage = null
                )
                onSuccess()
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
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
            val token = _uiState.value.token
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
}


