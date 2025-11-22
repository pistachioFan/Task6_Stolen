package com.example.four_practik.data.remote
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    @Volatile
    var baseUrl: String = "http://192.168.200.160:8080/api/"
}

object  ApiProvider {
    @Volatile
    private var retrofit: Retrofit? = null

    @Synchronized
    fun updateBaseUrl(newBaseUrl: String) {
        ApiConfig.baseUrl = newBaseUrl
        retrofit = null
    }

    private fun getRetrofit(): Retrofit {
        val cached = retrofit
        if (cached != null) return cached
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        val client = OkHttpClient.Builder().addInterceptor(logging).build()
        val created = Retrofit.Builder()
            .baseUrl(ApiConfig.baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit = created
        return created
    }

    val api: ReqResApi by lazy { getRetrofit().create(ReqResApi::class.java) }
}


