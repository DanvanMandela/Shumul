package com.craftsilicon.shumul.agency.data.bean.app.response

import com.google.gson.Gson
import javax.inject.Inject

class GlobalResponseSerializer @Inject constructor(private val gson: Gson) {
    fun convert(data: GlobalResponseData?): String? {
        return if (data == null) {
            null
        } else gson.toJson(data, GlobalResponseData::class.java)
    }

    fun convert(data: String?): GlobalResponseData? {
        return if (data == null) {
            null
        } else gson.fromJson(data, GlobalResponseData::class.java)
    }
}