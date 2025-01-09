package com.emrecevik.noroncontrolapp.interfaces

import com.emrecevik.noroncontrolapp.model.response.Devices
import com.emrecevik.noroncontrolapp.model.response.OtherClient
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface Device {
    @GET("device/getAllDevicesForClient")
    suspend fun getAllDevicesForClient(): Response<List<Devices>>

    @GET("device/getDeviceDetail")
    suspend fun getDeviceDetail(@Query("deviceId") devId: Long): Response<Devices>

    @POST("device/addUserToDevice")
    suspend fun addUserToDevice(@Query("devid") devId: Long, @Query("phone") phone: String) : Response<String?>

    @DELETE("device/removeUserFromDevice")
    suspend fun removeUserFromDevice(@Query("deviceId") deviceId: Long, @Query("phone") phone: String) : Response<String?>


    @GET("device/getUsersForDevice")
    suspend fun getUsersForDevice(@Query("deviceId") deviceId: Long): Response<List<OtherClient?>?>
}