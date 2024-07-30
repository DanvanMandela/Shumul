package com.craftsilicon.shumul.agency.ui.module.deposit

import android.content.Context
import com.craftsilicon.shumul.agency.R
import com.craftsilicon.shumul.agency.data.bean.action.ActionTypeEnum
import com.craftsilicon.shumul.agency.data.bean.app.response.GlobalResponseData
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

class DepositModuleResponse(
    response: HashMap<String, Any?>,
    val model: RemoteViewModelImpl,
    onError: (message: String?) -> Unit,
    onSuccess: (data: GlobalResponseData?) -> Unit,
    onToken:()->Unit
) {
    init {
        try {
            AppLogger.instance.appLog("VALIDATION:RESPONSE:ENCRYPTED", Gson().toJson(response))
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
                            val responseData = model.globalDataConverter.convert(it)
                            if (responseData != null) {
                                onSuccess(responseData)
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
            onError(e.message)
            e.printStackTrace()
        }
    }
}


fun depositFunc(
    toAccount: String,
    fromAccount: String,
    amount: String,
    context: Context,
    mobile: String,
    agentId: String,
    pin: String,
    narration: String,
    model: RemoteViewModelImpl
): PayloadRequest? {
    return try {
        val iv = model.deviceData!!.run
        val device = model.deviceData!!.device
        val uniqueID = getUniqueID()
        val unique=model.preferences.uniqueID.value
        val map = APP.data(
            context = context,
            storage = model.preferences,
            action = ActionTypeEnum.DEPOSIT.action,
            uniqueId = uniqueID
        )
        map["ACCOUNTID"] = fromAccount
        map["BANKACCOUNTID"] = toAccount
        map["MOBILENUMBER"] = mobile
        map["AGENTID"] = agentId
        map["TRXAMOUNT"] = amount
        map["TRXMPIN"] = Util.newEncrypt(pin)
        map["INFOFIELD1"] = narration
        map["CALLTYPE"] = "B-"
        AppLogger.instance.appLog("DEPOSIT:REQUEST", Gson().toJson(map))
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