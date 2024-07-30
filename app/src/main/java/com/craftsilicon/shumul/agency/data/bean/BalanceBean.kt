package com.craftsilicon.shumul.agency.data.bean

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import javax.inject.Inject
import javax.inject.Singleton

data class BalanceBean(
    @field:SerializedName("ClearBalance")
    @field:Expose
    val clear: String?,
    @field:SerializedName("AvailableBalance")
    @field:Expose
    val available: String?
)

data class BalanceResponse(
    @field:SerializedName("Message")
    @field:Expose
    val message: String?,
    @field:SerializedName("Data")
    @field:Expose
    val data: BalanceBean?,
)

@Singleton
class BalanceBeanTypeConverter @Inject constructor(private val gson: Gson) {
    fun convert(data: BalanceResponse?): String? {
        return if (data == null) {
            null
        } else gson.toJson(data, BalanceResponse::class.java)
    }

    fun convert(data: String?): BalanceResponse? {
        return if (data == null) {
            null
        } else gson.fromJson(data, BalanceResponse::class.java)
    }

}




