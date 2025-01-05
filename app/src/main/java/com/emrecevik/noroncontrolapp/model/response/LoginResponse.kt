package com.emrecevik.noroncontrolapp.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("refresh_token")
    @Expose
    val refresh_token: String,

    @SerializedName("access_token")
    @Expose
    val access_token: String
)