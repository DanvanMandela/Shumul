package com.craftsilicon.shumul.agency.data.bean.app.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GlobalResponseData(
    @field:SerializedName("Status")
    @field:Expose
    var status: String?,
    @field:SerializedName("Message")
    @field:Expose
    var message: String?,
    @field:SerializedName("Data")
    @field:Expose
    var data: Any?
)
