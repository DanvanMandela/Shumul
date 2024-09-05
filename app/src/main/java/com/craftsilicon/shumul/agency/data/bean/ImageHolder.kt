package com.craftsilicon.shumul.agency.data.bean

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import javax.inject.Inject
import javax.inject.Singleton

data class ImageHolder(
    @field:Expose
    @field:SerializedName("passport")
    var passport: String? = null,
    @field:Expose
    @field:SerializedName("idFront")
    var idFront: String? = null,
    @field:Expose
    @field:SerializedName("idBack")
    var idBack: String? = null,
    @field:Expose
    @field:SerializedName("signature")
    var signature: String? = null
)

@Singleton
class ImageHolderTypeConverter @Inject constructor(private val gson: Gson) {
    fun convert(data: ImageHolder?): String? {
        return if (data == null) {
            null
        } else gson.toJson(data, ImageHolder::class.java)
    }

    fun convert(data: String?): ImageHolder? {
        return if (data == null) {
            null
        } else gson.fromJson(data, ImageHolder::class.java)
    }

}
