package com.emrecevik.noroncontrolapp.model.requestBody

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SetRelay (
    @SerializedName("setrelay")
    @Expose
    val setrelay: String,

    @SerializedName("time")
    @Expose
    val time: String,


    @SerializedName("type")
    @Expose
    val type: String,

    @SerializedName("deviceId")
    @Expose
    val deviceId: Int,
)