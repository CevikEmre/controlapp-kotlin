package com.emrecevik.noroncontrolapp.service


import android.util.Log
import com.emrecevik.noroncontrolapp.interfaces.Client
import com.emrecevik.noroncontrolapp.model.requestBody.RegisterBody
import com.emrecevik.noroncontrolapp.model.response.LoginResponse
import com.emrecevik.noroncontrolapp.model.response.RegisterResponse
import com.emrecevik.noroncontrolapp.retrofit.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response


class ClientService {
    companion object {
        suspend fun login(username: String, password: String): Response<LoginResponse?>? {
            return withContext(Dispatchers.IO) {
                try {
                    val response = RetrofitClient.getClient()
                        .create(Client::class.java)
                        .login(username, password)
                    if (response.isSuccessful) {
                        response
                    } else {
                        val errorCode = response.code()
                        val errorMessage = response.errorBody()?.string()
                        Log.e(
                            "login",
                            "login request failed with code: $errorCode, message: $errorMessage"
                        )
                        null
                    }

                } catch (e: Exception) {
                    Log.e("LOGIN", "Error sending login request", e)
                    null
                }
            }
        }

        suspend fun register(registerBody: RegisterBody): Response<RegisterResponse?>? {
            return withContext(Dispatchers.IO) {
                try {
                    val response = RetrofitClient.getClient()
                        .create(Client::class.java)
                        .register(registerBody)
                    if (response.isSuccessful) {
                        response
                    } else {
                        val errorCode = response.code()
                        val errorMessage = response.errorBody()?.string()
                        Log.e(
                            "register",
                            "register request failed with code: $errorCode, message: $errorMessage"
                        )
                        null
                    }
                } catch (e: Exception) {
                    Log.e("REGISTER", "Error sending register request", e)
                    null
                }
            }
        }
        suspend fun refreshToken(refreshToken: String): Response<LoginResponse?>? {
            return withContext(Dispatchers.IO) {
                try {
                    val response = RetrofitClient.getClient()
                        .create(Client::class.java)
                        .refreshToken(refreshToken = refreshToken)
                    if (response.isSuccessful) {
                        response
                    } else {
                        val errorCode = response.code()
                        val errorMessage = response.errorBody()?.string()
                        Log.e(
                            "RefreshToken",
                            "RefreshToken request failed with code: $errorCode, message: $errorMessage"
                        )
                        null
                    }

                } catch (e: Exception) {
                    Log.e("RefreshToken", "Error sending login request", e)
                    null
                }
            }
        }
    }
}
