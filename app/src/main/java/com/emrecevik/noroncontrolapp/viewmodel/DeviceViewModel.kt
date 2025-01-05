package com.emrecevik.noroncontrolapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emrecevik.noroncontrolapp.model.response.GetAllDevicesForClient
import com.emrecevik.noroncontrolapp.service.DeviceService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DeviceViewModel : ViewModel() {
    private val _devices = MutableStateFlow<List<GetAllDevicesForClient?>?>(emptyList())
    val devices: StateFlow<List<GetAllDevicesForClient?>?> = _devices

    private val _device = MutableStateFlow<GetAllDevicesForClient?>(null)
    val device: StateFlow<GetAllDevicesForClient?> = _device

    val errorMessage = MutableStateFlow<String?>(null)

    var isLoading = mutableStateOf(false)

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
    fun getDeviceDetail(devId: Int) {
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
}
