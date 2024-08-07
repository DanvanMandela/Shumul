package com.craftsilicon.shumul.agency.ui.module.cash.account

import android.content.Context
import com.craftsilicon.shumul.agency.data.bean.action.ActionTypeEnum
import com.craftsilicon.shumul.agency.data.bean.app.route.PayloadRequest
import com.craftsilicon.shumul.agency.data.security.APP
import com.craftsilicon.shumul.agency.data.security.Util
import com.craftsilicon.shumul.agency.data.security.getUniqueID
import com.craftsilicon.shumul.agency.data.source.model.RemoteViewModelImpl
import com.craftsilicon.shumul.agency.ui.util.AppLogger
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject

object AccountToCashHelper {


    fun generate(
        name: String,
        account: String,
        amount: String,
        context: Context,
        mobile: String,
        narration: String,
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
                action = ActionTypeEnum.CASH_TO_CASH_REDEEM.action,
                uniqueId = uniqueID
            )

            map["ACCOUNTID"] = account
            map["MOBILENUMBER"] = mobile
            map["AGENTID"] = agentId
            map["TRXAMOUNT"] = amount
            map["TRXMPIN"] = Util.newEncrypt(pin)

            map["INFOFIELD102"] = mobile
            map["INFOFIELD3"] = "C2CRECEIVE"
            map["INFOFIELD102"] = account
            map["INFOFIELD4"] = amount
            map["INFOFIELD8"] = narration


            map["CALLTYPE"] = "B-"
            AppLogger.instance.appLog("C2C:REQUEST", Gson().toJson(map))
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

    fun redeem(
        traceNo: String,
        branch: String,
        account: String,
        amount: String,
        context: Context,
        mobile: String,
        otp: String,
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
                action = ActionTypeEnum.CASH_TO_CASH_REDEEM.action,
                uniqueId = uniqueID
            )

            map["ACCOUNTID"] = account
            map["MOBILENUMBER"] = mobile
            map["AGENTID"] = agentId
            map["TRXAMOUNT"] = amount
            map["TRXMPIN"] = Util.newEncrypt(pin)

            map["INFOFIELD102"] = mobile
            map["INFOFIELD3"] = "A2CPOST"
            map["INFOFIELD102"] = account
            map["INFOFIELD4"] = amount
            map["INFOFIELD108"] = otp
            map["INFOFIELD109"] = traceNo
            map["INFOFIELD110"] = branch


            map["CALLTYPE"] = "B-"
            AppLogger.instance.appLog("C2C:REQUEST", Gson().toJson(map))
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