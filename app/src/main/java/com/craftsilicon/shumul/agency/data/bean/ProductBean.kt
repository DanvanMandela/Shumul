package com.craftsilicon.shumul.agency.data.bean

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import javax.inject.Inject
import javax.inject.Singleton

data class ProductBean(
    @field:Expose
    @field:SerializedName("ProductID")
    val id: String?,
    @field:Expose
    @field:SerializedName("Description")
    val desc: String?,
    @field:Expose
    @field:SerializedName("ProductTypeID")
    val typeId: String?,
    @field:Expose
    @field:SerializedName("ProductTypeName")
    val name: String?,
    @field:Expose
    @field:SerializedName("MinAmount")
    val min: String?,
    @field:Expose
    @field:SerializedName("MaxAmount")
    val max: String?,
    @field:Expose
    @field:SerializedName("TermFrom")
    val termFrom: String?,
    @field:Expose
    @field:SerializedName("TermTo")
    val termTo: String?
)

data class ProductBeanResponse(
    @field:SerializedName("Data")
    @field:Expose
    val data: MutableList<ProductBean>?,
)

@Singleton
class ProductBeanTypeConverter @Inject constructor(private val gson: Gson) {
    fun convert(data: ProductBeanResponse?): String? {
        return if (data == null) {
            null
        } else gson.toJson(data, ProductBeanResponse::class.java)
    }
    fun convert(data: String?): ProductBeanResponse? {
        return if (data == null) {
            null
        } else gson.fromJson(data, ProductBeanResponse::class.java)
    }

}