package com.emrecevik.noroncontrolapp.navigation


sealed class Screen(
    val screen: String
){
    data object Login: Screen("loginScreen")
    data object Register: Screen("registerScreen")
    data object Main: Screen("mainScreen")
    data object DeviceDetails: Screen("deviceDetailScreen")
}
