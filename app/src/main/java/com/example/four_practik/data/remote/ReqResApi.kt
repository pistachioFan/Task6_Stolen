package com.example.four_practik.data.remote

import com.example.four_practik.data.remote.models.LoginRequest
import com.example.four_practik.data.remote.models.LoginResponse
import com.example.four_practik.data.remote.models.RegisterRequest
import com.example.four_practik.data.remote.models.RegisterResponse
import com.example.four_practik.data.remote.models.UsersResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ReqResApi {
    @POST("register")
    suspend fun register(@Body body: RegisterRequest): Response<RegisterResponse>

    @POST("login")
    suspend fun login(@Body body: LoginRequest): Response<LoginResponse>

    @GET("users")
    suspend fun getUsers(@Query("page") page: Int = 1): Response<UsersResponse>

    // No companion object; creation handled by ApiProvider with configurable baseUrl
}

