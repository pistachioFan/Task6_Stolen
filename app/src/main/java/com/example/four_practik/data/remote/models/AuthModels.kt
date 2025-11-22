package com.example.four_practik.data.remote.models

import com.google.gson.annotations.SerializedName

data class PersonDto(
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val birthDate: String,
    val gender: String,
    val groupId: Int
)

data class RegisterRequest(
    val login: String,
    val password: String,
    val email: String,
    val phoneNumber: String,
    val roleId: Int = 1,
    val authAllowed: Boolean = true,
    val person: PersonDto
)

data class RegisterResponse(
    val id: Int? = null,
    val token: String? = null
)

data class LoginRequest(
    val login: String,
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
    val userId: Int? = null,
    val login: String? = null,
    val email: String,
    val phoneNumber: String? = null,
    val roleId: Int? = null,
    val authAllowed: Boolean? = null,
    val personId: Int? = null,
    val createdDate: String? = null,
    val lastLoginDate: String? = null
)

data class GroupDto(
    @SerializedName("groupId")
    val id: Int,
    @SerializedName("groupName")
    val name: String
)

// In case the API wraps the response
data class GroupsResponse(
    val groups: List<GroupDto>? = null,
    val data: List<GroupDto>? = null
)


