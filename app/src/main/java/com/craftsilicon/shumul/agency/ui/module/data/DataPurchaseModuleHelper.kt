package com.craftsilicon.shumul.agency.ui.module.data

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

enum class DataStage {
    Validation, Pay
}

object DataPurchaseModuleHelper {

    fun pay(
        account: String,
        amount: String,
        mno: String,
        context: Context,
        mobile: String,
        agentId: String,
        lineType: String,
        offer: String,
        option: String,
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
            map["AGENTID"]=agentId
            map["TRXAMOUNT"] = amount //AMOUNT
            map["TRXMPIN"] = Util.newEncrypt(pin)
            map["INFOFIELD1"] =lineType
            map["INFOFIELD2"] = mno //MNO
            map["INFOFIELD3"] = offer
            map["INFOFIELD4"] = option
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



}