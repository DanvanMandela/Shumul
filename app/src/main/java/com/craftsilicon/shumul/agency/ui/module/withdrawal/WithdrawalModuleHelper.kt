package com.craftsilicon.shumul.agency.ui.module.withdrawal

import android.content.Context
import com.craftsilicon.shumul.agency.R
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

object WithdrawalModuleHelper{

    fun otpTransactionAgent(
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
                action = ActionTypeEnum.TRANSFER_OTP.action,
                uniqueId = uniqueID
            )
            map["ACCOUNTID"] = toAccount
            map["BANKACCOUNTID"] = fromAccount
            map["MOBILENUMBER"] = mobile
            map["AGENTID"] = agentId
            map["TRXAMOUNT"] = amount
            map["TRXMPIN"] = Util.newEncrypt(pin)
            map["INFOFIELD1"] = narration
            map["CALLTYPE"] = "B-"
            AppLogger.instance.appLog("WITHDRAWAL:REQUEST", Gson().toJson(map))
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


fun withdrawalFunc(
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
            action = ActionTypeEnum.WITHDRAWAL.action,
            uniqueId = uniqueID
        )
        map["ACCOUNTID"] = toAccount
        map["BANKACCOUNTID"] = fromAccount
        map["MOBILENUMBER"] = mobile
        map["AGENTID"]=agentId
        map["TRXAMOUNT"] = amount
        map["TRXMPIN"] = Util.newEncrypt(pin)
        map["INFOFIELD1"] = narration
        map["CALLTYPE"] = "B-"
        AppLogger.instance.appLog("WITHDRAWAL:REQUEST", Gson().toJson(map))
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


fun otpTransactionFunc(
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
            action = ActionTypeEnum.TRANSFER_OTP.action,
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
        AppLogger.instance.appLog("WITHDRAWAL:REQUEST", Gson().toJson(map))
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


fun otpTransactionCompleteFunc(
    toAccount: String,
    fromAccount: String,
    amount: String,
    context: Context,
    mobile: String,
    agentId: String,
    pin: String,
    otp: String,
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
            action = ActionTypeEnum.COMPLETE_TRANSFER_OTP.action,
            uniqueId = uniqueID
        )
        map["ACCOUNTID"] = toAccount
        map["BANKACCOUNTID"] = fromAccount
        map["MOBILENUMBER"] = mobile
        map["AGENTID"] = agentId
        map["TRXAMOUNT"] = amount
        map["INFOFIELD2"] = otp
        map["TRXMPIN"] = Util.newEncrypt(pin)
        map["INFOFIELD1"] = narration
        map["CALLTYPE"] = "B-"
        AppLogger.instance.appLog("WITHDRAWAL:REQUEST", Gson().toJson(map))
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


fun customerOtpTransactionCompleteFunc(
    toAccount: String,
    fromAccount: String,
    amount: String,
    context: Context,
    mobile: String,
    agentId: String,
    pin: String,
    otp: String,
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
            action = ActionTypeEnum.INTERNAL_TRANSFER_CUSTOMER.action,
            uniqueId = uniqueID
        )
        map["ACCOUNTID"] = fromAccount
        map["BANKACCOUNTID"] = toAccount
        map["MOBILENUMBER"] = mobile
        map["AGENTID"] = agentId
        map["TRXAMOUNT"] = amount
        map["INFOFIELD2"] = otp
        map["TRXMPIN"] = Util.newEncrypt(pin)
        map["CALLTYPE"] = "B-"
        AppLogger.instance.appLog("WITHDRAWAL:REQUEST", Gson().toJson(map))
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