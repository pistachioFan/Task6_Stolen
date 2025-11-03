package com.example.four_practik.data.remote.models

data class RegisterRequest(
    val email: String,
    val password: String
)

data class RegisterResponse(
    val id: Int?,
    val token: String?
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String?
)

data class UsersResponse(
    val page: Int,
    val per_page: Int,
    val total: Int,
    val total_pages: Int,
    val data: List<UserDto>
)

data class UserDto(
    val id: Int,
    val email: String,
    val first_name: String,
    val last_name: String,
    val avatar: String
)


