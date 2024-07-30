package com.craftsilicon.shumul.agency.data.bean.app.response

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import javax.inject.Inject
import javax.inject.Singleton

data class GlobalResponse(
    @field:SerializedName("Response")
    @field:Expose
    var response: String?
)

@Singleton
class GlobalResponseTypeConverter @Inject constructor(private val gson: Gson) {
    fun convert(data: GlobalResponse?): String? {
        return if (data == null) {
            null
        } else gson.toJson(data, GlobalResponse::class.java)
    }

    fun convert(data: String?): GlobalResponse? {
        return if (data == null) {
            null
        } else gson.fromJson(data, GlobalResponse::class.java)
    }

}
