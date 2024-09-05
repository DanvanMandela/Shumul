package com.craftsilicon.shumul.agency.ui.module.remittance

import android.content.Context
import com.craftsilicon.shumul.agency.R
import com.craftsilicon.shumul.agency.data.bean.CurrencyBean
import com.craftsilicon.shumul.agency.data.bean.ProductBean
import com.craftsilicon.shumul.agency.data.bean.action.ActionTypeEnum
import com.craftsilicon.shumul.agency.data.bean.app.response.GlobalResponseModule
import com.craftsilicon.shumul.agency.data.bean.app.route.PayloadRequest
import com.craftsilicon.shumul.agency.data.security.APP
import com.craftsilicon.shumul.agency.data.security.Util
import com.craftsilicon.shumul.agency.data.security.getUniqueID
import com.craftsilicon.shumul.agency.data.source.model.RemoteViewModelImpl
import com.craftsilicon.shumul.agency.ui.module.toBigNumberDisplay
import com.craftsilicon.shumul.agency.ui.util.AppLogger
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject

object RemittanceModuleHelper {

    fun currency(
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
                action = ActionTypeEnum.GET_CURRENCY.action,
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

    fun currencyResponse(
        response: HashMap<String, Any?>,
        model: RemoteViewModelImpl,
        onError: (message: String?) -> Unit,
        onSuccess: (data: MutableList<CurrencyBean>) -> Unit,
        onToken: () -> Unit
    ) {
        try {
            AppLogger.instance.appLog("ACCOUNT:OPENING:RESPONSE:ENCRYPTED", Gson().toJson(response))
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
                            val currency = model.currency.convert(it)
                            if (currency != null) {
                                currency.data?.let { it1 -> onSuccess(it1) }
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


    fun getFee(
        account: String,
        currency: String,
        amount: String,
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
                action = ActionTypeEnum.GET_FEES.action,
                uniqueId = uniqueID
            )
            map["AccountID"] = account
            map["MOBILENUMBER"] = mobile
            map["BANKACCOUNTID"] = account
            map["MOBILENUMBER"] = mobile
            map["INFOFIELD2"] = currency
            map["INFOFIELD1"] = amount
            map["AGENTID"] = agentId
            map["FUNCTIONNAME"] = "GETNAME"
            map["CALLTYPE"] = "M-"
            AppLogger.instance.appLog("FEE:REQUEST", Gson().toJson(map))
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

    fun search(
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
                action = ActionTypeEnum.SEARCH_REMITTANCE.action,
                uniqueId = uniqueID
            )
            map["AccountID"] = account
            map["MOBILENUMBER"] = mobile
            map["BANKACCOUNTID"] = account
            map["MOBILENUMBER"] = mobile
            map["AGENTID"] = agentId
            map["FUNCTIONNAME"] = "GETNAME"
            map["CALLTYPE"] = "M-"
            AppLogger.instance.appLog("FEE:REQUEST", Gson().toJson(map))
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


    fun remittanceAgent(
        account: String,
        data: HashMap<String, String>,
        context: Context,
        mobile: String,
        agentId: String,
        pin: String,
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
                action = ActionTypeEnum.REMITTANCE.action,
                uniqueId = uniqueID
            )



            map["AccountID"] = account
            map["MOBILENUMBER"] = mobile
            map["BANKACCOUNTID"] = account
            map["MOBILENUMBER"] = mobile
            map["AGENTID"] = agentId
            map["FUNCTIONNAME"] = "GETNAME"
            map["CALLTYPE"] = "M-"
            map["AMOUNT"] = data["charge"]
            map["TRXAMOUNT"] = data["charge"]
            map["INFOFIELD1"] = data["amount"]
            map["INFOFIELD2"] = data["currencyID"]
            map["INFOFIELD3"] = data["senderName"]
            map["INFOFIELD4"] = data["senderPhone"]
            map["INFOFIELD5"] = data["receiverName"]
            map["INFOFIELD6"] = data["receiverPhone"]
            map["INFOFIELD7"] = data["amount"]
            map["INFOFIELD8"] = data["narration"]
            map["INFOFIELD9"] = data["feeID"]
            map["INFOFIELD10"] = data["currencyID"]
            map["FUNCTIONNAME"] = "PAYBILL"
            map["TRXMPIN"] = Util.newEncrypt(pin)
            AppLogger.instance.appLog("REMITTANCE:REQUEST", Gson().toJson(map))
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

    fun remittanceCustomer(
        account: String,
        data: HashMap<String, String>,
        context: Context,
        mobile: String,
        agentId: String,
        pin: String,
        otp:String,
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
                action = ActionTypeEnum.REMITTANCE.action,
                uniqueId = uniqueID
            )


            map["AccountID"] = account
            map["MOBILENUMBER"] = mobile
            map["BANKACCOUNTID"] = account
            map["MOBILENUMBER"] = mobile
            map["AGENTID"] = agentId
            map["FUNCTIONNAME"] = "GETNAME"
            map["CALLTYPE"] = "M-"
            map["INFOFIELD1"] = data["amount"]
            map["INFOFIELD2"] = data["currencyID"]
            map["AMOUNT"] = data["charge"]
            map["TRXAMOUNT"] = data["charge"]
            map["INFOFIELD3"] = data["senderName"]
            map["INFOFIELD4"] = data["senderPhone"]
            map["INFOFIELD5"] = data["receiverName"]
            map["INFOFIELD6"] = data["receiverPhone"]
            map["INFOFIELD7"] = data["amount"]
            map["INFOFIELD8"] = data["narration"]
            map["INFOFIELD9"] = data["feeID"]
            map["INFOFIELD10"] = data["currencyID"]
            map["FUNCTIONNAME"] = "PAYBILL"
            map["TRXOTP"] = otp
            map["TRXMPIN"] = Util.newEncrypt(pin)
            AppLogger.instance.appLog("REMITTANCE:REQUEST", Gson().toJson(map))
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



//    {
//        "FormID": "VALIDATE",
//        "SessionID": "ffffffff-e46c-53ce-0000-00001d093e12",
//        "UNIQUEID": "26f41fd0-6698-11ef-96fe-19df6897fa8c",
//        "BankID": "9981",
//        "Country": "YEMENTEST",
//        "VersionNumber": "1.0.6",
//        "IMEI": "gzl6RdJjRM/wuCxnhwpLeYAqERDkPj2cN1psQCqGtjE=",
//        "IMSI": "gzl6RdJjRM/wuCxnhwpLeYAqERDkPj2cN1psQCqGtjE=",
//        "TRXSOURCE": "APP",
//        "APPNAME": "SHUMUL",
//        "CODEBASE": "ANDROID",
//        "CustomerID": "3114515620",
//        "LATLON": "0.00,0.00",
//        "ModuleID": "PAYREMITTANCE",
//        "MerchantID": "PAYREMITTANCE",
//        "Validate": {
//        "ACCOUNTID": "725412504",
//        "INFOFIELD1": "1",---REMCURRANCY1
//        "INFOFIELD2": "1",-----FEEID
//        "INFOFIELD9": "100",
//        "": "YER",
//        "INFOFIELD3": "986532741",----SENDERRPHONE
//        "INFOFIELD4": "9562384",----Receiver Phone no
//        "INFOFIELD6": "p U s h",----sender name
//        "INFOFIELD7": " k a t t",----receivername
//        "INFOFIELD8": "test",----comments
//        "INFOFIELD10": "010100003801"
//    }
//    }

    fun payRemittance(
        account: String,
        remittance: String,
        data: HashMap<String, String>,
        context: Context,
        mobile: String,
        agentId: String,
        pin: String,
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
                action = ActionTypeEnum.PAY_REMITTANCE.action,
                uniqueId = uniqueID
            )


            map["AccountID"] = account
            map["MOBILENUMBER"] = mobile
            map["BANKACCOUNTID"] = remittance
            map["MOBILENUMBER"] = mobile
            map["AGENTID"] = agentId
            map["FUNCTIONNAME"] = "GETNAME"
            map["CALLTYPE"] = "M-"
            map["INFOFIELD1"] = data["currencyID"]?.toBigNumberDisplay()
            map["AMOUNT"] = data["charge"]?.toBigNumberDisplay()
            map["TRXAMOUNT"] = data["charge"]?.toBigNumberDisplay()
            map["INFOFIELD6"] = data["senderName"]
            map["INFOFIELD3"] = data["senderPhone"]?.toBigNumberDisplay()
            map["INFOFIELD7"] = data["receiverName"]
            map["INFOFIELD4"] = data["receiverPhone"]?.toBigNumberDisplay()
            map["INFOFIELD9"] = data["amount"]?.toBigNumberDisplay()
            map["INFOFIELD8"] = "Comment"
            map["INFOFIELD2"] = data["feeID"]?.toBigNumberDisplay()
            map["INFOFIELD10"] = account
            map["TRXMPIN"] = Util.newEncrypt(pin)
            AppLogger.instance.appLog("REMITTANCE:REQUEST", Gson().toJson(map))
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


}

fun HashMap<String, Any?>.sumOf(): String {
    return "${this.entries.sumOf { it.value.toString().toDouble() }.toInt()}"
}

fun String.hasFourNames(): Boolean {
    val parts = this.trim().split("\\s+".toRegex())
    return parts.size == 4
}