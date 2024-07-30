package com.craftsilicon.shumul.agency.data.bean.app

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Payload(
    @field:SerializedName("MobileNumber")
    @field:Expose
    var mobileNumber: String?,
    @field:SerializedName("Device")
    @field:Expose
    var device: String?,
    @field:SerializedName("Ran")
    @field:Expose
    var ran: String?,
    @field:SerializedName("Routes")
    @field:Expose
    var routes: String?
)
