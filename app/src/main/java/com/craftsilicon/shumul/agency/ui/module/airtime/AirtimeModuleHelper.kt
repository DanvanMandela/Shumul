package com.craftsilicon.shumul.agency.ui.module.airtime

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

class AirtimeModuleResponse(
    response: HashMap<String, Any?>,
    val model: RemoteViewModelImpl,
    onError: (message: String?) -> Unit,
    onSuccess: (data: GlobalResponseData?) -> Unit,
    onToken: () -> Unit
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


fun airtimeFuc(
    account: String,
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
        val unique = model.preferences.uniqueID.value
        val map = APP.data(
            context = context,
            storage = model.preferences,
            action = ActionTypeEnum.BILL_PAYMENT.action,
            uniqueId = uniqueID
        )
        map["ACCOUNTID"] = mobile //MOBILE
        map["BANKACCOUNTID"] = account
        map["MOBILENUMBER"] = mobile
        map["AGENTID"] = agentId
        map["TRXAMOUNT"] = amount //AMOUNT
        map["TRXMPIN"] = Util.newEncrypt(pin)
        map["INFOFIELD1"] = account //LINE TYPE or 1
        map["INFOFIELD2"] = account //MNO
        map["INFOFIELD3"] = account //OFFER ID 240
        map["INFOFIELD4"] = account // renew
        map["CALLTYPE"] = "M-"
        map["FUNCTIONNAME"] = "PAYBILL"
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


fun mnoData(
    account: String,
    context: Context,
    mobile: String,
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
            action = ActionTypeEnum.GET_MNO.action,
            uniqueId = uniqueID
        )
        map["AccountID"] = account
        map["BANKACCOUNTID"] = account
        map["MOBILENUMBER"] = mobile
        map["AGENTID"] = agentId
        map["CALLTYPE"] = "O-"
        AppLogger.instance.appLog("ACCOUNT:OPENING:REQUEST", Gson().toJson(map))
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


fun airtimeValidate(
    account: String,
    context: Context,
    mobile: String,
    agentId: String,
    pin: String,
    mno: String,
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
            action = ActionTypeEnum.BILL_PAYMENT.action,
            uniqueId = uniqueID
        )
        map["ACCOUNTID"] = mobile //MOBILE
        map["BANKACCOUNTID"] = account
        map["MOBILENUMBER"] = mobile
        map["AGENTID"] = agentId
        map["TRXMPIN"] = Util.newEncrypt(pin)
        map["INFOFIELD1"] = mno //MNO
        map["INFOFIELD2"] = mobile // MOBILE
        map["CALLTYPE"] = "M-"
        map["FUNCTIONNAME"] = "GETNAME"
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


data class ReturnPackage(
    val feedback: String,
    val sentPackagesType: String,
    val exNumbers: String
)

val mnoList = listOf(
    ReturnPackage(
        feedback = "Yemen Mobile",
        sentPackagesType = "yemenmobile",
        exNumbers = "777212785"
    ),
    ReturnPackage(
        feedback = "Sabafon",
        sentPackagesType = "sabaphone",
        exNumbers = "718090401"
    ),
    ReturnPackage(
        feedback = "U",
        sentPackagesType = "you",
        exNumbers = "738453614"
    ),
    ReturnPackage(
        feedback = "Y",
        sentPackagesType = "y",
        exNumbers = "700856458"
    )
)
