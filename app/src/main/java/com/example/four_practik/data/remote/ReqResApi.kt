package com.example.four_practik.data.remote

import com.example.four_practik.data.remote.models.GroupDto
import com.example.four_practik.data.remote.models.LoginRequest
import com.example.four_practik.data.remote.models.LoginResponse
import com.example.four_practik.data.remote.models.RegisterRequest
import com.example.four_practik.data.remote.models.RegisterResponse
import com.example.four_practik.data.remote.models.UserDto
import com.example.four_practik.data.remote.models.UsersResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ReqResApi {
    @Headers("Content-Type: application/json")
    @POST("auth/register")
    suspend fun register(@Body body: RegisterRequest): Response<RegisterResponse>

    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): Response<LoginResponse>

    @GET("groups")
    suspend fun getGroups(): Response<List<GroupDto>>

    @GET("users")
    suspend fun getUsers(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1
    ): Response<List<UserDto>>

    // No companion object; creation handled by ApiProvider with configurable baseUrl
}

