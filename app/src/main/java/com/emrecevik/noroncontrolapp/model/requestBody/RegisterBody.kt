package com.emrecevik.noroncontrolapp.model.requestBody

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RegisterBody (
    @SerializedName("username")
    @Expose
    val username: String,

    @SerializedName("password")
    @Expose
    val password: String,

    @SerializedName("name")
    @Expose
    val name: String,

    @SerializedName("address")
    @Expose
    val address: String,

    @SerializedName("city")
    @Expose
    val city: String,

    @SerializedName("country")
    @Expose
    val country: String,

    @SerializedName("email")
    @Expose
    val email: String,

    @SerializedName("phone")
    @Expose
    val phone: String,

    @SerializedName("enable")
    @Expose
    val enable: Boolean,

    @SerializedName("deviceToken")
    @Expose
    val deviceToken: String,
)