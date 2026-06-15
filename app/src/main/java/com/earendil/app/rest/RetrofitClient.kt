package com.earendil.app.rest

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://earendil.ceticlub.fun/" // TODO: Change to real IP of database

    fun create(tokenProvider: () -> String?): ApiService {

        // El interceptor intercepta la petición antes de que salga
        val httpClient = OkHttpClient.Builder().addInterceptor { chain ->
            val originalRequest = chain.request()
            val token = tokenProvider() // Obtenemos el token guardado (de SharedPreferences)

            if (token != null) {
                // Si hay token, le clavamos el Header de forma automática
                val newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer $token")
                    .build()
                chain.proceed(newRequest)
            } else {
                chain.proceed(originalRequest)
            }
        }.build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient) // Le metemos nuestro cliente modificado
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}