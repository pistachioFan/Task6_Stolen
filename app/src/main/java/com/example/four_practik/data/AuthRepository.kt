package com.example.four_practik.data

import com.example.four_practik.data.remote.ApiProvider
import com.example.four_practik.data.remote.ReqResApi
import com.example.four_practik.data.remote.models.LoginRequest
import com.example.four_practik.data.remote.models.UserDto
import com.example.four_practik.data.remote.models.RegisterRequest

class AuthRepository(
    private val api: ReqResApi = ApiProvider.api
) {
    suspend fun register(email: String, password: String): Result<String> {
        return try {
            val response = api.register(RegisterRequest(email, password))
            if (response.isSuccessful) {
                val token = response.body()?.token
                if (!token.isNullOrBlank()) Result.success(token) else Result.failure(Exception("Empty token"))
            } else {
                Result.failure(Exception("Register failed: ${response.code()}"))
            }
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    suspend fun login(email: String, password: String): Result<String> {
        return try {
            val response = api.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                val token = response.body()?.token
                if (!token.isNullOrBlank()) Result.success(token) else Result.failure(Exception("Empty token"))
            } else {
                Result.failure(Exception("Login failed: ${response.code()}"))
            }
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    suspend fun getUsers(): Result<List<UserDto>> {
        return try {
            val response = api.getUsers()
            if (response.isSuccessful) {
                Result.success(response.body()?.data ?: emptyList())
            } else {
                Result.failure(Exception("Users failed: ${response.code()}"))
            }
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }
}


