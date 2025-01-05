package com.emrecevik.noroncontrolapp.service

import android.util.Log
import com.emrecevik.noroncontrolapp.interfaces.Relay
import com.emrecevik.noroncontrolapp.model.requestBody.SetRelay
import com.emrecevik.noroncontrolapp.retrofit.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class RelayService {
    companion object{
        suspend fun setRelay(setRelay: SetRelay): Response<Map<String, String>>? {
            return withContext(Dispatchers.IO) {
                try {
                    val response = RetrofitClient.getClient()
                        .create(Relay::class.java)
                        .setRelay(setRelay)
                    if (response.isSuccessful) {
                        response
                    } else {
                        val errorCode = response.code()
                        val errorMessage = response.errorBody()?.string()
                        Log.e(
                            "IOService",
                            "setRelay request failed with code: $errorCode, message: $errorMessage"
                        )
                        null
                    }

                } catch (e: Exception) {
                    Log.e("setRelay", "Error sending login request", e)
                    null
                }
            }
        }
    }
}