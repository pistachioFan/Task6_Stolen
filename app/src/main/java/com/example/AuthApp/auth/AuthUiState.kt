package com.example.AuthApp.auth

import com.example.AuthApp.data.remote.models.GroupDto
import com.example.AuthApp.data.remote.models.UserDto

data class AuthUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val token: String? = null,
    val users: List<UserDto> = emptyList(),
    val groups: List<GroupDto> = emptyList()
)
