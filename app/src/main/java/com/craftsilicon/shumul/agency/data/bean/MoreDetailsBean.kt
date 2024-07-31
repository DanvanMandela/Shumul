package com.craftsilicon.shumul.agency.data.bean

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import javax.inject.Inject
import javax.inject.Singleton

data class MoreDetailsBean(
    @field:Expose
    val sector: String?,
    @field:Expose
    val education: String?,
    val country:String?,
    val address:String?,
    @field:Expose
    val job: String?,
    @field:Expose
    val employer: String?,
    @field:Expose
    val income: String?,
    @field:Expose
    val monthly: String?,
    @field:Expose
    val withdraw: String?,
    @field:Expose
    val deposit: String?,
    @field:Expose
    val residence: String?,
    @field:Expose
    val emergency: String?,
    @field:Expose
    val city: String?,
    @field:Expose
    val occupation: String?,
    @field:Expose
    val total: String?,
    @field:Expose
    val landline: String?,
)

@Singleton
class MoreDetailsTypeConverter @Inject constructor(private val gson: Gson) {
    fun convert(data: MoreDetailsBean?): String? {
        return if (data == null) {
            null
        } else gson.toJson(data, MoreDetailsBean::class.java)
    }

    fun convert(data: String?): MoreDetailsBean? {
        return if (data == null) {
            null
        } else gson.fromJson(data, MoreDetailsBean::class.java)
    }

}