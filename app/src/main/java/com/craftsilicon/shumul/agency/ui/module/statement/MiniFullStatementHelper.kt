package com.craftsilicon.shumul.agency.ui.module.statement

import android.content.Context
import android.util.Log
import com.craftsilicon.shumul.agency.R
import com.craftsilicon.shumul.agency.data.bean.BalanceBean
import com.craftsilicon.shumul.agency.data.bean.MiniData
import com.craftsilicon.shumul.agency.data.bean.action.ActionTypeEnum
import com.craftsilicon.shumul.agency.data.bean.app.response.GlobalResponseModule
import com.craftsilicon.shumul.agency.data.bean.app.route.PayloadRequest
import com.craftsilicon.shumul.agency.data.security.APP
import com.craftsilicon.shumul.agency.data.security.Util
import com.craftsilicon.shumul.agency.data.security.getUniqueID
import com.craftsilicon.shumul.agency.data.source.model.RemoteViewModelImpl
import com.craftsilicon.shumul.agency.ui.util.AppLogger
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Locale

class MiniStatementModuleResponse(
    response: HashMap<String, Any?>,
    val model: RemoteViewModelImpl,
    onError: (message: String?) -> Unit,
    onSuccess: (data: MiniData?) -> Unit,
    onToken: () -> Unit
) {
    init {
        try {
            AppLogger.instance.appLog("MINI:STATEMENT:RESPONSE:ENCRYPTED", Gson().toJson(response))
            if (response.containsKey("Response")) {
                val global = model.globalResponseConverter
                    .convert(model.any.convert(response))
                if (global != null)
                    GlobalResponseModule(
                        response = global,
                        storage = model.preferences,
                        security = model.restApiSecurity,
                        serializer = model.globalDataConverter,
                        onError = onError,
                        onSuccess = {
                            val mini = model.mini.convert(it)
                            if (mini != null) {
                                onSuccess(mini)
                            } else onError(
                                "${
                                    response["message"] ?: model.resourceProvider
                                        .getString(R.string.something_went_wrong)
                                }"
                            )
                        }, onToken = onToken
                    )
                else onError(
                    "${
                        response["message"] ?: model.resourceProvider
                            .getString(R.string.something_went_wrong)
                    }"
                )
            } else onError(
                "${
                    response["message"] ?: model.resourceProvider
                        .getString(R.string.something_went_wrong)
                }"
            )
        } catch (e: Exception) {
            onError(
                model.resourceProvider
                    .getString(R.string.something_went_wrong)
            )
            e.printStackTrace()
        }
    }
}


fun miniFunc(
    account: String,
    context: Context,
    mobile: String,
    pin: String,
    agentId: String,
    model: RemoteViewModelImpl
): PayloadRequest? {
    return try {
        val iv = model.deviceData!!.run
        val device = model.deviceData!!.device
        val uniqueID = getUniqueID()
        val unique = model.preferences.uniqueID.value
        val map = APP.data(
            context = context,
            storage = model.preferences,
            action = ActionTypeEnum.MINI.action,
            uniqueId = uniqueID
        )
        map["AccountID"] = account
        map["BANKACCOUNTID"] = account
        map["MOBILENUMBER"] = mobile
        map["AGENTID"]=agentId
        map["TRXPIN"] = Util.newEncrypt(pin)
        map["CALLTYPE"] = "B-"
        AppLogger.instance.appLog("MINI:REQUEST", Gson().toJson(map))
        PayloadRequest(
            uniqueId = "$unique",
            data = model.restApiSecurity.encrypt(
                value = JSONObject(map).toString(),
                kv = device!!,
                iv = iv!!
            )
        )
    } catch (ex: JSONException) {
        ex.printStackTrace()
        null
    }
}


fun fullFunc(
    from: Long,
    to: Long,
    account: String,
    context: Context,
    mobile: String,
    pin: String,
    agentId: String,
    model: RemoteViewModelImpl
): PayloadRequest? {
    return try {
        val iv = model.deviceData!!.run
        val device = model.deviceData!!.device
        val uniqueID = getUniqueID()
        val unique = model.preferences.uniqueID.value
        val map = APP.data(
            context = context,
            storage = model.preferences,
            action = ActionTypeEnum.FULL.action,
            uniqueId = uniqueID
        )
        map["AccountID"] = account
        map["BANKACCOUNTID"] = account
        map["MOBILENUMBER"] = mobile
        map["INFOFIELD1"] = from.toElmaDate()
        map["INFOFIELD3"] = to.toElmaDate()
        map["AGENTID"] = agentId
        map["TRXPIN"] = Util.newEncrypt(pin)
        map["CALLTYPE"] = "B-"
        AppLogger.instance.appLog("MINI:REQUEST", Gson().toJson(map))
        PayloadRequest(
            uniqueId = "$unique",
            data = model.restApiSecurity.encrypt(
                value = JSONObject(map).toString(),
                kv = device!!,
                iv = iv!!
            )
        )
    } catch (ex: JSONException) {
        ex.printStackTrace()
        null
    }
}

val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

fun Long.toElmaDate(): String {
    return outputFormat.format(this)
}