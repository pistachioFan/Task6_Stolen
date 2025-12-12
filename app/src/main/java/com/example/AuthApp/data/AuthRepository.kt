package com.example.AuthApp.data

import android.util.Log
import com.example.AuthApp.data.remote.ApiProvider
import com.example.AuthApp.data.remote.ReqResApi
import com.example.AuthApp.data.remote.models.AuthResponse
import com.example.AuthApp.data.remote.models.GroupDto
import com.example.AuthApp.data.remote.models.LoginRequest
import com.example.AuthApp.data.remote.models.PersonDto
import com.example.AuthApp.data.remote.models.UserDto
import com.example.AuthApp.data.remote.models.RegisterRequest
import retrofit2.Response

class AuthRepository(
    private val api: ReqResApi = ApiProvider.api
) {
    suspend fun register(
        login: String,
        email: String,
        password: String,
        phoneNumber: String,
        firstName: String,
        lastName: String,
        patronymic: String,
        dateOfBirth: String,
        gender: String,
        groupId: Int
    ): Result<String> {
        return try {
            val person = PersonDto(
                firstName = firstName,
                lastName = lastName,
                middleName = patronymic,
                birthDate = dateOfBirth,
                gender = gender,
                groupId = groupId
            )
            val request = RegisterRequest(
                login = login,
                password = password,
                email = email,
                phoneNumber = phoneNumber,
                roleId = 1, // Always 1, not changeable
                authAllowed = true,
                person = person
            )

            val response = api.register(request)
            handleAuthResponse(response)
        } catch (t: Throwable) {
            if (t.message?.contains("BEGIN_OBJECT") == true || t.message?.contains("BEGIN_ARRAY") == true) {
                Result.failure(Exception("JSON parsing error - check if API response format matches expected model. Error: ${t.message}"))
            } else {
                Result.failure(t)
            }
        }
    }

    suspend fun login(login: String,
                      password: String): Result<String> {
        return try {
            val response = api.login(LoginRequest(login, password))
            handleAuthResponse(response)
        } catch (t: Throwable) {
            if (t.message?.contains("BEGIN_OBJECT") == true || t.message?.contains("BEGIN_ARRAY") == true) {
                Result.failure(Exception("JSON parsing error - check if API response format matches expected model. Error: ${t.message}"))
            } else {
                Result.failure(t)
            }
        }
    }

    suspend fun getGroups(): Result<List<GroupDto>> {
        return try {
            val response = api.getGroups()
            if (response.isSuccessful) {
                val body = response.body()
                // Handle direct list response
                if (body is List<*>) {
                    @Suppress("UNCHECKED_CAST")
                    Result.success(body ?: emptyList())
                } else {
                    // Handle wrapped response or empty
                    Result.success(emptyList())
                }
            } else {
                Result.failure(Exception("Groups failed: ${response.code()} - ${response.message()}"))
            }
        } catch (t: Throwable) {
            Result.failure(Exception("Groups error: ${t.message}", t))
        }
    }

    suspend fun getUsers(token: String): Result<List<UserDto>> {
        return try {
            val response = api.getUsers("Bearer $token")
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("AuthRepository", "Users failed: HTTP ${response.code()} - ${response.message()}")
                Log.e("AuthRepository", "Error body: $errorBody")
                Result.failure(Exception("Users failed: HTTP ${response.code()} - ${response.message()}${if (!errorBody.isNullOrBlank()) "\n$errorBody" else ""}"))
            }
        } catch (t: Throwable) {
            Log.e("AuthRepository", "Users exception: ${t.message}", t)
            if (t.message?.contains("BEGIN_OBJECT") == true || t.message?.contains("BEGIN_ARRAY") == true) {
                Result.failure(Exception("JSON parsing error - check if API response format matches expected model. Error: ${t.message}"))
            } else {
                Result.failure(t)
            }
        }
    }
    private fun handleAuthResponse(response: Response<AuthResponse>): Result<String>{
        val responseCode = response.code()
        return if (response.isSuccessful) {
            try {
                val responseBody = response.body()
                val token = responseBody?.token
                if (!token.isNullOrBlank()) {
                    Result.success(token)
                } else {
                    Result.failure(Exception("Empty token. Response code: $responseCode"))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Parse error: ${e.message}"))
            }
        } else {
            val errorBody = response.errorBody()?.string()
            Result.failure(Exception("Authorization failed: HTTP $responseCode - ${response.message()}${if (!errorBody.isNullOrBlank()) "\n$errorBody" else ""}"))
        }
    }
}


