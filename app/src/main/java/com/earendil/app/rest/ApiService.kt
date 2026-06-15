package com.earendil.app.rest

import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ------ AUTHENTICATION ------
    // - LOGIN
    @POST("/login")
    suspend fun loginWithUserAndPassword(@Body body: LoginRequest): Response<LoginResponse>

    @POST("/login-google")
    suspend fun loginWithGoogle(@Body body: GoogleLoginRequest): Response<LoginResponse>

    // - REGISTER
    @POST("/usr-new-register")
    suspend fun registerUser(@Body body: RegisterNewUser): Response<RegisterResponse>

    // - LOGOUT
    @POST("/logout")
    suspend fun logoutUser(): Response<Confirmation>


    // Ruta protegida enviando el Token en los Headers de forma nativa en móviles
//    @GET("/protected-route")
//    suspend fun getProtectedData(@Header("Authorization") token: String): Response<DataResponse>
}


// DATA CLASSES // DATA CLASSES // DATA CLASSES // DATA CLASSES // DATA CLASSES // DATA CLASSES //
       // DATA CLASSES // DATA CLASSES // DATA CLASSES // DATA CLASSES // DATA CLASSES // DATA CLASSES //
// DATA CLASSES // DATA CLASSES // DATA CLASSES // DATA CLASSES // DATA CLASSES // DATA CLASSES //

// ------ AUTHENTICATION ------
// - LOGIN
data class GoogleLoginRequest(val idToken: String)
data class LoginRequest(val username: String, val password: String)
data class LoginResponse(val successful: Boolean, val reason: String, val token: String, val username: String)


// - REGISTER
data class RegisterNewUser(val username: String, val mail: String, val password: String, val mcAccount: String)
data class RegisterResponse(val successful: Boolean, val reason: String)

// - LOGOUT
data class Confirmation(val successful: Boolean)