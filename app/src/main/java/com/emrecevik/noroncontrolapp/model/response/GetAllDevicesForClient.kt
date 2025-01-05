package com.emrecevik.noroncontrolapp.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetAllDevicesForClient(
    @SerializedName("id")
    @Expose
    val id: Long,

    @SerializedName("devId")
    @Expose
    val devId: Long,

    @SerializedName("enable")
    @Expose
    val enable: Boolean,

    @SerializedName("clientId")
    @Expose
    val clientId: Long,

    @SerializedName("otherClients")
    @Expose
    val otherClients: List<OtherClient>,

    @SerializedName("createdDateTime")
    @Expose
    val createdDateTime: String,

    @SerializedName("activatedDateTime")
    @Expose
    val activatedDateTime: String,

    @SerializedName("activeDays")
    @Expose
    val activeDays: Long,

    @SerializedName("yearlyPrice")
    @Expose
    val yearlyPrice: Long,

    @SerializedName("m2mNumber")
    @Expose
    val m2mNumber: String,

    @SerializedName("m2mSerial")
    @Expose
    val m2mSerial: String,

    @SerializedName("connected")
    @Expose
    val connected: Boolean,

    @SerializedName("deviceType")
    @Expose
    val deviceType: String,
)

data class OtherClient(
    @SerializedName("id")
    @Expose
    val id: Long,

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
)

