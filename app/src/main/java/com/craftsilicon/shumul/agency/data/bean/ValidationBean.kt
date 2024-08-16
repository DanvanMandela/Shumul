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
    @field:SerializedName("TrxAmount")
    @field:Expose
    val amount: String?,
    @field:SerializedName("ToMobile")
    @field:Expose
    val toMobile: String?,
    @field:SerializedName("ToName")
    @field:Expose
    val toName: String?,
    @field:SerializedName("FromMobile")
    @field:Expose
    val fromMobile: String?,
    @field:SerializedName("FromName")
    @field:Expose
    val fromName: String?,
    @field:SerializedName("FromAccountID")
    @field:Expose
    val fromAccount: String?,
) {
    var holderAmount: String? = null
    var account: String? = null
    var otpHolder: String? = null
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
