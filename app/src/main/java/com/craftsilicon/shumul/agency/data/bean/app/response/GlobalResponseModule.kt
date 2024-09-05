package com.craftsilicon.shumul.agency.data.bean.app.response

import com.craftsilicon.shumul.agency.data.bean.app.StatusEnum
import com.craftsilicon.shumul.agency.data.security.encry.RestApiSecurity
import com.craftsilicon.shumul.agency.data.source.storage.pref.StorageDataSource
import com.craftsilicon.shumul.agency.ui.util.AppLogger
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

class GlobalResponseModule(
    response: GlobalResponse,
    storage: StorageDataSource,
    security: RestApiSecurity,
    serializer: GlobalResponseSerializer,
    onError: (message: String?) -> Unit,
    onSuccess: (data: String?) -> Unit,
    onToken: () -> Unit
) {
    init {
        try {
            val decrypted = security.decrypt(
                value = "${response.response}",
                kv = "${storage.deviceData.value!!.device}",
                iv = "${storage.deviceData.value!!.run}"
            )
            val globalResponse = serializer.convert(decrypted)
            AppLogger.instance.appLog("GLOBAL:RESPONSE:DECRYPTED", decrypted)
            when (globalResponse?.status) {
                StatusEnum.SUCCESS.type -> onSuccess(decrypted)
                StatusEnum.TOKEN.type -> CoroutineScope(Dispatchers.Main).launch { onToken() }
                else -> onError(globalResponse?.message)
            }
        } catch (e: Exception) {
            onError(e.message)
            e.printStackTrace()
        }
    }
}

class GlobalResponseModuleNot(
    response: String,
    storage: StorageDataSource,
    security: RestApiSecurity,
    serializer: GlobalResponseSerializer,
    onError: (message: String?) -> Unit,
    onSuccess: (data: String?) -> Unit,
    onToken: () -> Unit
) {
    init {
        try {
            val globalResponse = serializer.convert(response)
            AppLogger.instance.appLog("GLOBAL:RESPONSE:DECRYPTED", "$response")
            when (globalResponse?.status) {
                StatusEnum.SUCCESS.type -> onSuccess(response)
                StatusEnum.TOKEN.type -> CoroutineScope(Dispatchers.Main).launch { onToken() }
                else -> onError(globalResponse?.message)
            }
        } catch (e: Exception) {
            onError(e.message)
            e.printStackTrace()
        }
    }
}

fun String?.bodyToMap(): HashMap<String, Any?> {
    val map = HashMap<String, Any?>()
    this?.let {
        try {
            val gson = Gson()
            val jsonMap = gson.fromJson<Map<String, Any?>>(this, HashMap::class.java)
            map.putAll(jsonMap)
        } catch (e: Exception) {
            map["error"] = this
        }
    }
    return map
}

@Singleton
class AnyConverter @Inject constructor(private val gson: Gson) {

    fun convert(data: Any?): String? {
        return if (data == null) {
            null
        } else gson.toJson(data, Any::class.java)
    }

    fun convert(str: String?): Any? {
        return if (str == null) {
            null
        } else gson.fromJson(str, Any::class.java)
    }

    fun map(data: String?): HashMap<String, Any?> {
        val type = object : TypeToken<HashMap<String, Any?>>() {}.type
        return data?.let {
            gson.fromJson(it, type)
        } ?: hashMapOf()
    }

    fun mapPrimitive(data: String?): HashMap<String, String?> {
        val type = object : TypeToken<HashMap<String, String?>>() {}.type
        return data?.let {
            gson.fromJson(it, type)
        } ?: hashMapOf()
    }

}