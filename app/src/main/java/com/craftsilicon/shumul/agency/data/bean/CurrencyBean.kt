package com.craftsilicon.shumul.agency.data.bean

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import javax.inject.Inject
import javax.inject.Singleton

data class CurrencyBean(
    @field:Expose
    @field:SerializedName("CurrencyID")
    val id: String?,
    @field:Expose
    @field:SerializedName("Description")
    val description: String?
)


data class CurrencyBeanResponse(
    @field:SerializedName("Data")
    @field:Expose
    val data: MutableList<CurrencyBean>?,
)

@Singleton
class CurrencyBeanTypeConverter @Inject constructor(private val gson: Gson) {
    fun convert(data: CurrencyBeanResponse?): String? {
        return if (data == null) {
            null
        } else gson.toJson(data, CurrencyBeanResponse::class.java)
    }

    fun convert(data: String?): CurrencyBeanResponse? {
        return if (data == null) {
            null
        } else gson.fromJson(data, CurrencyBeanResponse::class.java)
    }

}
