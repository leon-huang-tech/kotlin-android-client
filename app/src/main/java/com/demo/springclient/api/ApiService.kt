package com.demo.springclient.api

import com.demo.springclient.model.LoginRequest
import com.demo.springclient.model.Order
import com.demo.springclient.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import okhttp3.ResponseBody
import retrofit2.http.Query
interface ApiService {

    @POST("api/users/login")
    suspend fun login(@Body request: LoginRequest): Map<String, String>

    @GET("api/users")
    suspend fun getUsers(@Header("Authorization") token: String): List<User>

    @GET("api/orders")
    suspend fun getOrders(@Header("Authorization") token: String): List<Order>

    @GET("api/ai/chat/stream")
    suspend fun chatStream(
        @Header("Authorization") token: String,
        @Query("message") message: String,
        @Query("sessionId") sessionId: String
    ): ResponseBody
}