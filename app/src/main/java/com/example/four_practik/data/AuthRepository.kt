package com.example.four_practik.data

import android.util.Log
import com.example.four_practik.data.remote.ApiProvider
import com.example.four_practik.data.remote.ReqResApi
import com.example.four_practik.data.remote.models.GroupDto
import com.example.four_practik.data.remote.models.GroupsResponse
import com.example.four_practik.data.remote.models.LoginRequest
import com.example.four_practik.data.remote.models.PersonDto
import com.example.four_practik.data.remote.models.UserDto
import com.example.four_practik.data.remote.models.RegisterRequest
import com.google.gson.Gson

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
    ): Result<Pair<String, Int>> {
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
            
            // Log the JSON being sent
            val gson = Gson()
            val jsonRequest = gson.toJson(request)
            Log.d("AuthRepository", "Register Request JSON: $jsonRequest")
            Log.d("AuthRepository", "Group ID being sent: $groupId")
            
            val response = api.register(request)
            val responseCode = response.code()
            
            // Log response
            Log.d("AuthRepository", "Register Response Code: $responseCode")
            
            if (response.isSuccessful) {
                try {
                    val responseBody = response.body()
                    val token = responseBody?.token
                    Log.d("AuthRepository", "Register Response Token: $token")
                    if (!token.isNullOrBlank()) {
                        Result.success(Pair(token, responseCode))
                    } else {
                        Log.e("AuthRepository", "Empty token received. Response code: $responseCode")
                        Result.failure(Exception("Empty token. Response code: $responseCode"))
                    }
                } catch (e: Exception) {
                    Log.e("AuthRepository", "Error parsing register response: ${e.message}", e)
                    Log.e("AuthRepository", "Exception type: ${e.javaClass.simpleName}")
                    Result.failure(Exception("Parse error: ${e.message}"))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("AuthRepository", "Register failed: HTTP $responseCode - ${response.message()}")
                Log.e("AuthRepository", "Error body: $errorBody")
                Result.failure(Exception("Register failed: HTTP $responseCode - ${response.message()}${if (!errorBody.isNullOrBlank()) "\n$errorBody" else ""}"))
            }
        } catch (t: Throwable) {
            Log.e("AuthRepository", "Register exception: ${t.message}", t)
            Log.e("AuthRepository", "Exception type: ${t.javaClass.simpleName}", t)
            if (t.message?.contains("BEGIN_OBJECT") == true || t.message?.contains("BEGIN_ARRAY") == true) {
                Result.failure(Exception("JSON parsing error - check if API response format matches expected model. Error: ${t.message}"))
            } else {
                Result.failure(t)
            }
        }
    }

    suspend fun login(login: String, password: String): Result<Pair<String, Int>> {
        return try {
            val response = api.login(LoginRequest(login, password))
            val responseCode = response.code()
            
            if (response.isSuccessful) {
                try {
                    val responseBody = response.body()
                    val token = responseBody?.token
                    Log.d("AuthRepository", "Login Response Token: $token")
                    if (!token.isNullOrBlank()) {
                        Result.success(Pair(token, responseCode))
                    } else {
                        Log.e("AuthRepository", "Empty token received. Response code: $responseCode")
                        Result.failure(Exception("Empty token. Response code: $responseCode"))
                    }
                } catch (e: Exception) {
                    Log.e("AuthRepository", "Error parsing login response: ${e.message}", e)
                    Log.e("AuthRepository", "Exception type: ${e.javaClass.simpleName}")
                    Result.failure(Exception("Parse error: ${e.message}"))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("AuthRepository", "Login failed: HTTP $responseCode - ${response.message()}")
                Log.e("AuthRepository", "Error body: $errorBody")
                Result.failure(Exception("Login failed: HTTP $responseCode - ${response.message()}${if (!errorBody.isNullOrBlank()) "\n$errorBody" else ""}"))
            }
        } catch (t: Throwable) {
            Log.e("AuthRepository", "Login exception: ${t.message}", t)
            Log.e("AuthRepository", "Exception type: ${t.javaClass.simpleName}", t)
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
                    Result.success(body as? List<GroupDto> ?: emptyList())
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
}


