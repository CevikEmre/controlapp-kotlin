package com.emrecevik.noroncontrolapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emrecevik.noroncontrolapp.model.requestBody.SetRelay
import com.emrecevik.noroncontrolapp.service.RelayService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RelayViewModel : ViewModel() {

    private val _relayResponse = MutableStateFlow<Map<String, String>?>(null)
    val relayResponse: StateFlow<Map<String, String>?> = _relayResponse

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun setRelay(setRelay: SetRelay) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RelayService.setRelay(setRelay)

                if (response?.isSuccessful == true) {
                    // Başarılı yanıtı işleme
                    val responseBody = response.body()
                    _relayResponse.value = responseBody
                    _errorMessage.value = null
                    Log.i("RelayViewModel", "Başarılı Yanıt: $responseBody")
                } else {
                    // Hata durumunu işleme
                    val errorBody = response?.errorBody()?.string()
                        ?: "Bilinmeyen bir hata oluştu."
                    _errorMessage.value = errorBody
                    _relayResponse.value = null
                    Log.e("RelayViewModel", "Hata Yanıtı: $errorBody")
                }
            } catch (e: Exception) {
                // Beklenmeyen hataları işleme
                val exceptionMessage = "Relay komutu gönderilirken bir hata oluştu: ${e.localizedMessage}"
                _errorMessage.value = exceptionMessage
                _relayResponse.value = null
                Log.e("RelayViewModel", exceptionMessage, e)
            } finally {
                _isLoading.value = false
                Log.d("RelayViewModel", "İşlem tamamlandı. Yükleme durumu: ${_isLoading.value}")
            }
        }
    }
}
