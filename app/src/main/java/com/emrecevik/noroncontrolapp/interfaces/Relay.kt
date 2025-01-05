package com.emrecevik.noroncontrolapp.interfaces

import com.emrecevik.noroncontrolapp.model.requestBody.SetRelay
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface Relay {
    @POST("io/send-command")
    suspend fun setRelay(@Body setRelay: SetRelay): Response<Map<String, String>>
}