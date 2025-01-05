package com.emrecevik.noroncontrolapp.viewmodel
import android.util.Log
import androidx.lifecycle.ViewModel
import com.emrecevik.noroncontrolapp.NoroncontrolappApplication
import com.emrecevik.noroncontrolapp.model.requestBody.RegisterBody
import com.emrecevik.noroncontrolapp.model.response.LoginResponse
import com.emrecevik.noroncontrolapp.model.response.RegisterResponse
import com.emrecevik.noroncontrolapp.service.ClientService
import com.emrecevik.noroncontrolapp.session.SessionManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Response

class ClientViewModel : ViewModel() {
    private val sessionManager = SessionManager(context = NoroncontrolappApplication.Companion.appContext())
    var errorMessage: String? = null

    suspend fun login(username: String, password: String): Boolean {
        return try {
            val response: Response<LoginResponse?>? = ClientService.login(username, password)
            if (response?.isSuccessful == true) {
                val loginResponse = response.body()
                if (loginResponse != null) {
                    sessionManager.saveTokens(
                        loginResponse.access_token,
                        loginResponse.refresh_token
                    )
                    Log.e("LoginVM", "Login successful! AccessToken: ${loginResponse.access_token}")
                    errorMessage = null // Başarılı durumda hata mesajını temizle
                    true
                } else {
                    errorMessage = "Sunucudan geçersiz veri alındı."
                    Log.e("LoginVM", "Response body is null")
                    false
                }
            } else {
                errorMessage = extractErrorMessage(response)
                Log.e("LoginVM", "Login failed with code: ${response?.code()} and message: $errorMessage")
                false
            }
        } catch (e: Exception) {
            errorMessage = "Bir hata oluştu: ${e.localizedMessage}"
            Log.e("LoginVM", "Error during login", e)
            false
        }
    }

    suspend fun register(registerBody: RegisterBody): Boolean {
        return try {
            val response: Response<RegisterResponse?>? = ClientService.register(registerBody)
            if (response?.isSuccessful == true) {
                Log.e("RegisterVM", "Register successful!")
                errorMessage = null // Başarılı durumda hata mesajını temizle
                true
            } else {
                errorMessage = extractErrorMessage(response)
                Log.e("RegisterVM", "Register failed with code: ${response?.code()} and message: $errorMessage")
                false
            }
        } catch (e: Exception) {
            errorMessage = "Bir hata oluştu: ${e.localizedMessage}"
            Log.e("RegisterVM", "Error during register", e)
            false
        }
    }

    private fun extractErrorMessage(response: Response<*>?): String {
        return try {
            val errorBody = response?.errorBody()?.string()
            val type = object : TypeToken<Map<String, String>>() {}.type
            val errorMap: Map<String, String> = Gson().fromJson(errorBody, type)
            errorMap["error"] ?: "Bilinmeyen bir hata oluştu."
        } catch (e: Exception) {
            "Hata mesajı ayrıştırılamadı."
        }
    }
}
