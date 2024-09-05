package com.craftsilicon.shumul.agency.ui.module.login

import android.content.Context
import com.craftsilicon.shumul.agency.R
import com.craftsilicon.shumul.agency.data.bean.AppUserState
import com.craftsilicon.shumul.agency.data.bean.action.ActionTypeEnum
import com.craftsilicon.shumul.agency.data.bean.app.response.GlobalResponseModule
import com.craftsilicon.shumul.agency.data.bean.app.route.PayloadRequest
import com.craftsilicon.shumul.agency.data.security.APP.data
import com.craftsilicon.shumul.agency.data.security.Util
import com.craftsilicon.shumul.agency.data.security.getUniqueID
import com.craftsilicon.shumul.agency.data.source.model.RemoteViewModelImpl
import com.craftsilicon.shumul.agency.ui.util.AppLogger
import com.craftsilicon.shumul.agency.ui.util.countryCode
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject

class LoginModuleResponse(
    response: HashMap<String, Any?>,
    val model: RemoteViewModelImpl,
    type: LoginType = LoginType.Login,
    onError: (message: String?) -> Unit,
    onSuccess: (message: String?) -> Unit,
    onToken: () -> Unit
) {
    init {
        try {
            AppLogger.instance.appLog("LOGIN:RESPONSE:ENCRYPTED", Gson().toJson(response))
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
                            val user = model.user.convert(it)
                            if (user != null) {
                                model.preferences.appUserState(
                                    AppUserState(
                                        agent = "${user.account.singleOrNull()?.agentID}",
                                        mobile = "${user.mobile}"
                                    )
                                )
                                model.preferences.userData(user)
                            }
                            if (type == LoginType.Activation)
                                model.preferences.login(true)
                            onSuccess(
                                "${model.resourceProvider.getString(R.string.welcome_back_)} " +
                                        "${user?.firstName}"
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

fun loginFunc(
    username: String,
    mobile: String,
    pin: String,
    context: Context,
    model: RemoteViewModelImpl
): PayloadRequest? {
    return try {
        val iv = model.deviceData!!.run
        val device = model.deviceData!!.device
        val uniqueIdLocal = getUniqueID()
        val uniqueIdGlobal = model.preferences.uniqueID.value
        val map = data(
            context = context,
            storage = model.preferences,
            action = ActionTypeEnum.LOGIN.action,
            uniqueId = uniqueIdLocal
        )
        map["CUSTOMERID"] = username
        map["AGENTID"] = username
        map["LOGINMPIN"] = Util.newEncrypt(pin)
        map["MOBILENUMBER"] = mobile
        map["LOGINTYPE"] = "PIN"
        AppLogger.instance.appLog("LOGIN:REQUEST", Gson().toJson(map))
        PayloadRequest(
            uniqueId = "$uniqueIdGlobal",
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