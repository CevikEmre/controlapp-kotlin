package com.emrecevik.noroncontrolapp.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("username")
    @Expose
    val username: String,

    @SerializedName("text")
    @Expose
    val text: String,

    @SerializedName("confirmed")
    @Expose
    val confirmed :String,

    @SerializedName("phone")
    @Expose
    val phone: String,
)
