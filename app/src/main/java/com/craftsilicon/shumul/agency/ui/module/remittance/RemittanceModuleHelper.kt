package com.craftsilicon.shumul.agency.ui.module.remittance

import android.content.Context
import com.craftsilicon.shumul.agency.R
import com.craftsilicon.shumul.agency.data.bean.ProductBean
import com.craftsilicon.shumul.agency.data.bean.action.ActionTypeEnum
import com.craftsilicon.shumul.agency.data.bean.app.response.GlobalResponseModule
import com.craftsilicon.shumul.agency.data.bean.app.route.PayloadRequest
import com.craftsilicon.shumul.agency.data.security.APP
import com.craftsilicon.shumul.agency.data.security.getUniqueID
import com.craftsilicon.shumul.agency.data.source.model.RemoteViewModelImpl
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
            map["CALLTYPE"] = "B-"
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
        onSuccess: (data: MutableList<ProductBean>) -> Unit,
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
                            val products = model.product.convert(it)
                            if (products != null) {
                                products.data?.let { it1 -> onSuccess(it1) }
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
}