package com.craftsilicon.shumul.agency.data.bean

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import javax.inject.Inject
import javax.inject.Singleton

data class ValidationBean(
    @field:SerializedName("ClientName")
    @field:Expose
    var clientName: String?,
    @field:SerializedName("AccountName")
    @field:Expose
    val accountName: String?,
    @field:SerializedName("CurrencyID")
    @field:Expose
    var currency: String?,
    @field:SerializedName("Photo")
    @field:Expose
    val avatar: String?,
    @field:SerializedName("TraceNo")
    @field:Expose
    val traceNo: String?,
    @field:SerializedName("Mobile")
    @field:Expose
    val mobile: String?,
    @field:SerializedName("TrxTraceNo")
    @field:Expose
    val tracNo: String?,
    @field:SerializedName("OurBranchID")
    @field:Expose
    val branch: String?,
) {
    var amount: String? = null
    var account: String? = null
    var extra = hashMapOf<String, Any?>()
}


data class ValidationResponse(
    @field:SerializedName("Data")
    @field:Expose
    val data: ValidationBean?,
)

@Singleton
class ValidationResponseTypeConverter @Inject constructor(private val gson: Gson) {
    fun convert(data: ValidationResponse?): String? {
        return if (data == null) {
            null
        } else gson.toJson(data, ValidationResponse::class.java)
    }

    fun convert(data: String?): ValidationResponse? {
        return if (data == null) {
            null
        } else gson.fromJson(data, ValidationResponse::class.java)
    }

}
