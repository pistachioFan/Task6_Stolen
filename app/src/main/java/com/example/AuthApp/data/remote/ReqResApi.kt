package com.example.AuthApp.data.remote

import com.example.AuthApp.data.remote.models.GroupDto
import com.example.AuthApp.data.remote.models.LoginRequest
import com.example.AuthApp.data.remote.models.AuthResponse
import com.example.AuthApp.data.remote.models.RegisterRequest
import com.example.AuthApp.data.remote.models.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ReqResApi {
    @POST("auth/register")
    suspend fun register(@Body body: RegisterRequest): Response<AuthResponse>

    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): Response<AuthResponse>

    @GET("groups")
    suspend fun getGroups(): Response<List<GroupDto>>

    @GET("users")
    suspend fun getUsers(): Response<List<UserDto>>

    // No companion object; creation handled by ApiProvider with configurable baseUrl
}

