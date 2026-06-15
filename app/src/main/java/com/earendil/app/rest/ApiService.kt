package com.earendil.app.rest

import com.earendil.app.models.Publicacion
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    // ------ ROUTES ------
    @Multipart
    @POST("/api/posts/create")
    suspend fun crearPublicacion(
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part image: MultipartBody.Part? // El archivo puede ser opcional (null)
    ): Response<PostResponse>

    @GET("/api/posts")
    suspend fun getPosts(@Query("search") search: String?): Response<GetPostsResponse>

    @PUT("/api/posts/{id}")
    suspend fun updatePost(
        @Path("id") id: String,
        @Body body: UpdatePostRequest
    ): Response<ActionResponse>

    @DELETE("/api/posts/{id}")
    suspend fun deletePost(@Path("id") id: String): Response<ActionResponse>

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


// ------ ROUTES ------

data class PostResponse(val successful: Boolean, val reason: String)
data class GetPostsResponse(val successful: Boolean, val posts: List<Publicacion>?, val reason: String?)
data class ActionResponse(val successful: Boolean, val reason: String)
data class UpdatePostRequest(val description: String)