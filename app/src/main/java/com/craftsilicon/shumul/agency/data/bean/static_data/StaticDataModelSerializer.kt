package com.craftsilicon.shumul.agency.data.bean.static_data

import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StaticDataModelSerializer  @Inject constructor(private val gson: Gson) {

    fun convert(data: StaticDataModel?): String? {
        return if (data == null) {
            null
        } else gson.toJson(data, StaticDataModel::class.java)
    }

    fun convert(data: String?): StaticDataModel? {
        return if (data == null) {
            null
        } else gson.fromJson(data, StaticDataModel::class.java)
    }
}