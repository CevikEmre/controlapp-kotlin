package com.emrecevik.noroncontrolapp.interfaces

import com.emrecevik.noroncontrolapp.model.requestBody.DeviceInfoBody
import com.emrecevik.noroncontrolapp.model.requestBody.SetRelay
import com.emrecevik.noroncontrolapp.model.response.DeviceInfoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface Relay {
    @POST("io/send-command")
    suspend fun setRelay(@Body setRelay: SetRelay): Response<Map<String, String>>

    @POST("io/getDeviceInfo")
    suspend fun getDeviceInfo(@Body deviceInfoBody: DeviceInfoBody): Response<DeviceInfoResponse?>
}