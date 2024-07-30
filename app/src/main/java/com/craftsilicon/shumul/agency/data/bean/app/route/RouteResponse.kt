package com.craftsilicon.shumul.agency.data.bean.app.route

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RouteResponse(
    @field:SerializedName("Data")
    @field:Expose
    val data: String?,
    @field:SerializedName("respCode")
    @field:Expose
    val respCode: String?,
    @field:SerializedName("message")
    @field:Expose
    val message: String?
)
