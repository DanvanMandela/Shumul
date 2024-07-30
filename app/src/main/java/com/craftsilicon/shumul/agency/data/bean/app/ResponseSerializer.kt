package com.craftsilicon.shumul.agency.data.bean.app

import com.google.gson.Gson
import javax.inject.Inject

class ResponseSerializer @Inject constructor(private val gson: Gson) {
    fun convert(data: AppResponse?): String? {
        return if (data == null) {
            null
        } else gson.toJson(data, AppResponse::class.java)
    }

    fun convert(data: String?): AppResponse? {
        return if (data == null) {
            null
        } else gson.fromJson(data, AppResponse::class.java)
    }
}