package com.craftsilicon.shumul.agency.data.bean.device

import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceDataTypeConverter @Inject constructor(private val gson: Gson) {
    fun convert(data: DeviceData?): String? {
        return if (data == null) {
            null
        } else gson.toJson(data, DeviceData::class.java)
    }

    fun convert(data: String?): DeviceData? {
        return if (data == null) {
            null
        } else gson.fromJson(data, DeviceData::class.java)
    }

}