package com.craftsilicon.shumul.agency.data.bean.app

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AppResponse(
    @field:SerializedName("respCode")
    @field:Expose
    var respCode: String?,
    @field:SerializedName("message")
    @field:Expose
    var message: String?,
    @field:SerializedName("token")
    @field:Expose
    var token: String?,
    @field:SerializedName("data")
    @field:Expose
    var data: MutableList<Int>?,
    @field:SerializedName("payload")
    @field:Expose
    var payload: Payload?,
    @field:SerializedName("requestStatus")
    @field:Expose
    var requestStatus: String?,
    @field:SerializedName("Response")
    @field:Expose
    var response: String?
)


