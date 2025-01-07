package com.emrecevik.noroncontrolapp.interfaces

import com.emrecevik.noroncontrolapp.model.requestBody.RegisterBody
import com.emrecevik.noroncontrolapp.model.response.ClientDetails
import com.emrecevik.noroncontrolapp.model.response.LoginResponse
import com.emrecevik.noroncontrolapp.model.response.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface Client {
    @POST("client/checkClient")
    suspend fun login(@Query("username") username: String, @Query("password") password: String): Response<LoginResponse?>

    @POST("client/saveNewClient")
    suspend fun register(@Body registerBody: RegisterBody): Response<RegisterResponse?>

    @POST("client/refreshToken")
    suspend fun refreshToken(@Query("refreshToken") refreshToken: String): Response<LoginResponse?>

    @GET("client/clientDetails")
    suspend fun getClientDetails(@Header("Authorization") token: String?): Response<ClientDetails?>?
}