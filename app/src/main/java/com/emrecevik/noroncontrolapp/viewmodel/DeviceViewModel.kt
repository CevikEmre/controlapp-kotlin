package com.emrecevik.noroncontrolapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emrecevik.noroncontrolapp.model.response.Devices
import com.emrecevik.noroncontrolapp.model.response.OtherClient
import com.emrecevik.noroncontrolapp.service.DeviceService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DeviceViewModel : ViewModel() {
    private val _devices = MutableStateFlow<List<Devices?>?>(emptyList())
    val devices: StateFlow<List<Devices?>?> = _devices

    private val _users = MutableStateFlow<List<OtherClient?>?>(emptyList())
    val users: StateFlow<List<OtherClient?>?> = _users

    private val _device = MutableStateFlow<Devices?>(null)
    val device: StateFlow<Devices?> = _device

    val errorMessage = MutableStateFlow<String?>(null)
    val addUserErrorMessage = MutableStateFlow<String?>(null)

    var isLoading = mutableStateOf(false)
    var isRefreshing = mutableStateOf(false)

    private val _addSuccess = MutableStateFlow(false)
    val addSuccess: StateFlow<Boolean> = _addSuccess

    private val _addError = MutableStateFlow(false)
    var addError: StateFlow<Boolean> = _addError


    val responseMessage = MutableStateFlow<String?>(null)

    fun fetchDevices() {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = DeviceService.getAllDevicesForClient()
                if (response?.isSuccessful == true) {
                    _devices.value = response.body() ?: emptyList()
                    errorMessage.value = null
                } else {
                    errorMessage.value = response?.errorBody()?.string() ?: "Sunucudan hata alındı."
                }
            } catch (e: Exception) {
                errorMessage.value = "Cihazları alırken bir hata oluştu: ${e.localizedMessage}"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun getDeviceDetail(devId: Long) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = DeviceService.getDeviceDetail(devId)
                if (response?.isSuccessful == true) {
                    _device.value = response.body()
                    errorMessage.value = null
                } else {
                    errorMessage.value = response?.errorBody()?.string() ?: "Sunucudan hata alındı."
                }
            } catch (e: Exception) {
                errorMessage.value = "Cihazları alırken bir hata oluştu: ${e.localizedMessage}"
            } finally {
                isLoading.value = false
            }
        }
    }

    suspend fun addUserToDevice(devId: Long, phone: String) {
        isRefreshing.value = true
        try {
            val response = DeviceService.addUserToDevice(devId, phone)
            responseMessage.value = response // Başarılı yanıt mesajı
            addUserErrorMessage.value = null
            _addSuccess.value = true
            _addError.value = false
        } catch (e: Exception) {
            responseMessage.value = "Hata: ${e.localizedMessage}"
            addUserErrorMessage.value = e.localizedMessage
            _addSuccess.value = false
            _addError.value = true
        } finally {
            isRefreshing.value = false
        }
    }

    suspend fun removeUserFromDevice(devId: Long, phone: String) {
        isRefreshing.value = true
        try {
            val response = DeviceService.removeUserFromDevice(devId, phone)
            responseMessage.value = response // Başarılı yanıt mesajı
            addUserErrorMessage.value = null
            _addSuccess.value = true
            _addError.value = false
        } catch (e: Exception) {
            responseMessage.value = "Hata: ${e.localizedMessage}"
            addUserErrorMessage.value = e.localizedMessage
            _addSuccess.value = false
            _addError.value = true
        } finally {
            isRefreshing.value = false
        }
    }




    fun getUsersForDevice(deviceId: Long) {
        viewModelScope.launch {
            isRefreshing.value = true
            try {
                // Servis katmanındaki fonksiyonu çağırıyoruz
                val response = DeviceService.getUsersForDevice(deviceId)
                if (response != null) {
                    _users.value = response
                    errorMessage.value = null
                } else {
                    errorMessage.value = "Sunucudan geçerli bir yanıt alınamadı."
                }
            } catch (e: Exception) {
                errorMessage.value = "Cihaz kullanıcılarını alırken bir hata oluştu: ${e.localizedMessage}"
            } finally {
                isRefreshing.value = false
            }
        }
    }


    fun resetStates() {
        _addSuccess.value = false
        _addError.value = false
    }
}

