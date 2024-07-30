package com.craftsilicon.shumul.agency.data.bean.route

import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RouteModelSerializer {
    @set:Inject
    lateinit var gson: Gson

    fun convert(data: RouteModel?): String? {
        return if (data == null) {
            null
        } else gson.toJson(data, RouteModel::class.java)
    }

    fun convert(str: String?): RouteModel? {
        return if (str == null) {
            null
        } else gson.fromJson(str, RouteModel::class.java)
    }
}