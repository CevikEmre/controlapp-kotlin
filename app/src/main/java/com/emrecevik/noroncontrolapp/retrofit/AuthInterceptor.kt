package com.emrecevik.noroncontrolapp.retrofit

import com.emrecevik.noroncontrolapp.NoroncontrolappApplication
import com.emrecevik.noroncontrolapp.service.ClientService
import com.emrecevik.noroncontrolapp.session.SessionManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthInterceptor : Interceptor {
    private val sessionManager = SessionManager(NoroncontrolappApplication.Companion.appContext())

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val url = originalRequest.url.toString()
        val accessToken = sessionManager.getAccessToken()

        val modifiedRequest = if (!url.contains("/api/client/") && accessToken != null) {
            originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .build()
        } else {
            originalRequest
        }

        val response = chain.proceed(modifiedRequest)

        if (response.code == 401) {
            response.close()

            val newAccessToken = refreshAccessToken()
            if (newAccessToken != null) {
                val retriedRequest = addAuthorizationHeader(originalRequest, newAccessToken)
                return chain.proceed(retriedRequest)
            }
        }

        return response
    }

    private fun refreshAccessToken(): String? {
        val refreshToken = sessionManager.getRefreshToken() ?: return null

        return runBlocking {
            try {
                val refreshResponse = ClientService.refreshToken(refreshToken)
                if (refreshResponse != null) {
                    if (refreshResponse.isSuccessful) {
                        val loginResponse = refreshResponse.body()
                        if (loginResponse != null) {
                            sessionManager.saveTokens(
                                loginResponse.access_token,
                                loginResponse.refresh_token
                            )
                            return@runBlocking loginResponse.access_token
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            null
        }
    }

    private fun addAuthorizationHeader(request: Request, token: String): Request {
        return request.newBuilder()
            .removeHeader("Authorization")
            .addHeader("Authorization", "Bearer $token")
            .build()
    }
}
