package com.craftsilicon.shumul.agency.ui.module.cash.cash

import android.content.Context
import com.craftsilicon.shumul.agency.R
import com.craftsilicon.shumul.agency.data.bean.ValidationBean
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

object CashToCashHelper {


    fun generate(
        toName: String,
        fromName: String,
        fromAccount: String,
        amount: String,
        context: Context,
        mobile: String,
        toMobile: String,
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
                action = ActionTypeEnum.CASH_TO_CASH_GENERATE.action,
                uniqueId = uniqueID
            )

//            IF @MerchantID = 'CASHTOCASH'
//            BEGIN
//            SET @MessageType = '0200'
//            INSERT INTO #Response SELECT 2, @MobileNumber
//            INSERT INTO #Response SELECT 3, 'C2C'
//            INSERT INTO #Response SELECT 4, @Amount
//            INSERT INTO #Response SELECT 7, @MonthPart + @DayPart + @HourPart + @MinPart + @SecPart
//            INSERT INTO #Response SELECT 11, @StanNumber
//            INSERT INTO #Response SELECT 49, 'YER'
//            INSERT INTO #Response SELECT 102, @AccountID--agentAccountID
//            INSERT INTO #Response SELECT 106, @InfoField1--trx description
//            INSERT INTO #Response SELECT 107, @InfoField3--ToMobile(Receiver Mob)
//            INSERT INTO #Response SELECT 108, @InfoField2--ToName
//            INSERT INTO #Response SELECT 109, @Infofield4--FromName


            map["ACCOUNTID"] = fromAccount //Debit
            map["BANKACCOUNTID"] = fromAccount //Credit

            map["MOBILENUMBER"] = mobile
            map["AGENTID"] = agentId
            map["TRXAMOUNT"] = amount
            map["TRXMPIN"] = Util.newEncrypt(pin)

            map["INFOFIELD1"] = narration
            map["INFOFIELD4"] = fromName
            map["INFOFIELD2"] = toName
            map["INFOFIELD3"] = toMobile


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
        name: String,
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



//            IF @MerchantID = 'C2CRECEIVE'
//            BEGIN
//
//            SET @MessageType = '0200'
//            INSERT INTO #Response SELECT 3, 'C2CRECEIVE'
//            INSERT INTO #Response SELECT 4, @Amount
//            INSERT INTO #Response SELECT 7, @MonthPart + @DayPart + @HourPart + @MinPart + @SecPart
//            INSERT INTO #Response SELECT 11, @StanNumber
//            INSERT INTO #Response SELECT 49, 'YER'
//            INSERT INTO #Response SELECT 102, @AccountID-- Agent account
//            INSERT INTO #Response SELECT 108, @InfoField1--OTP




            map["ACCOUNTID"] = account //Debit
            map["BANKACCOUNTID"] = account //Credit

            map["MOBILENUMBER"] = mobile
            map["AGENTID"] = agentId
            map["TRXAMOUNT"] = amount
            map["TRXMPIN"] = Util.newEncrypt(pin)

            map["INFOFIELD102"] = mobile
            map["INFOFIELD3"] = "C2CRECEIVE"
            map["INFOFIELD102"] = account
            map["INFOFIELD4"] = amount
            map["INFOFIELD1"] = otp


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


    fun response(
        response: HashMap<String, Any?>,
        model: RemoteViewModelImpl,
        onError: (message: String?) -> Unit,
        onSuccess: (data: ValidationBean?) -> Unit,
        onToken: () -> Unit
    ) {
        try {
            AppLogger.instance.appLog("C2C:RESPONSE:ENCRYPTED", Gson().toJson(response))
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
                            val validation = model.validation.convert(it)
                            if (validation != null) {
                                onSuccess(validation.data)
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