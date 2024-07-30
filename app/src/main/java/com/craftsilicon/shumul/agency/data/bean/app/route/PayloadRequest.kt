package com.craftsilicon.shumul.agency.data.bean.app.route

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PayloadRequest(
    @field:SerializedName("UniqueId")
    @field:Expose
    var uniqueId: String = String(),
    @field:SerializedName("Data")
    @field:Expose
    var data: String
)
