package com.example.AuthApp.data.remote
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    @Volatile
    var baseUrl: String = "http://192.168.200.160:8080/api/"
}

object  ApiProvider {

    @Volatile
    private var retrofit: Retrofit? = null
    private fun getRetrofit(): Retrofit {
        val interceptor =  AuthInterceptor()
        val client = OkHttpClient
                    .Builder()
                    .addInterceptor(interceptor)
                    .build()
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


