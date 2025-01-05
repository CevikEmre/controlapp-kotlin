package com.emrecevik.noroncontrolapp.service

import android.util.Log
import com.emrecevik.noroncontrolapp.interfaces.Device
import com.emrecevik.noroncontrolapp.model.response.GetAllDevicesForClient
import com.emrecevik.noroncontrolapp.retrofit.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class DeviceService {
    companion object {
        suspend fun getAllDevicesForClient(): Response<List<GetAllDevicesForClient>>? {
            return withContext(Dispatchers.IO) {
                try {
                    val response = RetrofitClient.getClient()
                        .create(Device::class.java)
                        .getAllDevicesForClient()
                    if (response.isSuccessful) {
                        response
                    } else {
                        val errorCode = response.code()
                        val errorMessage = response.errorBody()?.string()
                        Log.e(
                            "DeviceService",
                            "getAllDevicesForClient request failed with code: $errorCode, message: $errorMessage"
                        )
                        null
                    }

                } catch (e: Exception) {
                    Log.e("GetAllDevices", "Error sending login request", e)
                    null
                }
            }
        }
        suspend fun getDeviceDetail(devId: Int): Response<GetAllDevicesForClient>?{
            return withContext(Dispatchers.IO) {
                try {
                    val response = RetrofitClient.getClient()
                        .create(Device::class.java)
                        .getDeviceDetail(devId)
                    if (response.isSuccessful) {
                        response
                    } else {
                        val errorCode = response.code()
                        val errorMessage = response.errorBody()?.string()
                        Log.e(
                            "DeviceService",
                            "getDeviceDetail request failed with code: $errorCode, message: $errorMessage"
                        )
                        null
                    }

                } catch (e: Exception) {
                    Log.e("getDeviceDetail", "Error sending getDeviceDetail request", e)
                    null
                }
            }
        }
    }
}