package com.craftsilicon.shumul.agency.data.bean

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import javax.inject.Inject
import javax.inject.Singleton


data class WorkSectorBean(
    @field:Expose
    @field:SerializedName("ID")
    val id: String?,
    @field:Expose
    @field:SerializedName("Name")
    val name: String?
)

data class WorkSectorBeanResponse(
    @field:SerializedName("Data")
    @field:Expose
    val data: MutableList<WorkSectorBean>?,
)


@Singleton
class WorkSectorBeanTypeConverter @Inject constructor(private val gson: Gson) {
    fun convert(data: WorkSectorBeanResponse?): String? {
        return if (data == null) {
            null
        } else gson.toJson(data, WorkSectorBeanResponse::class.java)
    }
    fun convert(data: String?): WorkSectorBeanResponse? {
        return if (data == null) {
            null
        } else gson.fromJson(data, WorkSectorBeanResponse::class.java)
    }

}