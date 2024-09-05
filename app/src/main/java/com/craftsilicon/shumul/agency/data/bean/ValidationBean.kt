package com.craftsilicon.shumul.agency.data.bean

import com.craftsilicon.shumul.agency.data.bean.app.response.AnyConverter
import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import javax.inject.Inject
import javax.inject.Singleton

data class ValidationBean(
    @field:SerializedName("ClientName")
    @field:Expose
    var clientName: String? = null,
    @field:SerializedName("AccountName")
    @field:Expose
    val accountName: String? = null,
    @field:SerializedName("CurrencyID")
    @field:Expose
    var currency: String? = null,
    @field:SerializedName("Photo")
    @field:Expose
    val avatar: String? = null,
    @field:SerializedName("TraceNo")
    @field:Expose
    val traceNo: String? = null,
    @field:SerializedName("Mobile")
    @field:Expose
    val mobile: String? = null,
    @field:SerializedName("TrxTraceNo")
    @field:Expose
    val tracNo: String? = null,
    @field:SerializedName("OurBranchID")
    @field:Expose
    val branch: String? = null,
    @field:SerializedName("TrxAmount")
    @field:Expose
    var amount: String? = null,
    @field:SerializedName("ToMobile")
    @field:Expose
    val toMobile: String? = null,
    @field:SerializedName("ToName")
    @field:Expose
    val toName: String? = null,
    @field:SerializedName("FromMobile")
    @field:Expose
    val fromMobile: String? = null,
    @field:SerializedName("FromName")
    @field:Expose
    val fromName: String? = null,
    @field:SerializedName("FromAccountID")
    @field:Expose
    val fromAccount: String? = null,
) {
    var holderAmount: String? = null
    var account: String? = null
    var otpHolder: String? = null
    var isOtp: Boolean = false
    var extra = hashMapOf<String, Any?>()
    var display = linkedMapOf<String, Any?>()
}


data class ValidationResponse(
    @field:SerializedName("Data")
    @field:Expose
    val data: ValidationBean?,
)

data class ValidationHashResponse(
    @field:SerializedName("Data")
    @field:Expose
    val data: HashMap<String, Any?>,
)

@Singleton
class ValidationResponseTypeConverter @Inject constructor(
    private val gson: Gson,
    private val any: AnyConverter
) {
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

    fun toHash(data: String?): ValidationHashResponse? {
        return if (data == null) {
            null
        } else gson.fromJson(data, ValidationHashResponse::class.java)
    }

}
