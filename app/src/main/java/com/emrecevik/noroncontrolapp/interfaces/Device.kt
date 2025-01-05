package com.emrecevik.noroncontrolapp.interfaces

import com.emrecevik.noroncontrolapp.model.response.GetAllDevicesForClient
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Device {
    @GET("device/getAllDevicesForClient")
    suspend fun getAllDevicesForClient(): Response<List<GetAllDevicesForClient>>

    @GET("device/getDeviceDetail")
    suspend fun getDeviceDetail(@Query("deviceId") devId: Int): Response<GetAllDevicesForClient>

}