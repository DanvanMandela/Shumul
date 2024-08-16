package com.craftsilicon.shumul.agency.ui.module.account

import android.content.Context
import android.util.Base64
import android.util.Log
import com.craftsilicon.shumul.agency.R
import com.craftsilicon.shumul.agency.data.bean.AccountOpening
import com.craftsilicon.shumul.agency.data.bean.ProductBean
import com.craftsilicon.shumul.agency.data.bean.WorkSectorBean
import com.craftsilicon.shumul.agency.data.bean.action.ActionTypeEnum
import com.craftsilicon.shumul.agency.data.bean.app.response.GlobalResponseModule
import com.craftsilicon.shumul.agency.data.bean.app.response.GlobalResponseModuleNot
import com.craftsilicon.shumul.agency.data.bean.app.route.PayloadRequest
import com.craftsilicon.shumul.agency.data.security.APP
import com.craftsilicon.shumul.agency.data.security.getUniqueID
import com.craftsilicon.shumul.agency.data.source.model.RemoteViewModelImpl
import com.craftsilicon.shumul.agency.ui.util.AppLogger
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject

class AccountOpeningModuleProductResponse(
    response: HashMap<String, Any?>,
    val model: RemoteViewModelImpl,
    onError: (message: String?) -> Unit,
    onSuccess: (data: MutableList<ProductBean>) -> Unit,
    onToken: () -> Unit
) {
    init {
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
}

class AccountOpeningModuleResponse(
    response: HashMap<String, Any?>,
    val model: RemoteViewModelImpl,
    onError: (message: String?) -> Unit,
    onSuccess: (data: String?) -> Unit,
    onToken: () -> Unit
) {
    init {
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
                            val data = model.globalDataConverter.convert(it)
                            if (data != null) {
                                onSuccess(data.message)
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


class AccountOpeningModuleResponseNot(
    response: HashMap<String, Any?>,
    val model: RemoteViewModelImpl,
    onError: (message: String?) -> Unit,
    onSuccess: (data: String?) -> Unit,
    onToken: () -> Unit
) {
    init {
        try {

            AppLogger.instance.appLog("ACCOUNT:OPENING:RESPONSE:ENCRYPTED", Gson().toJson(response))
            GlobalResponseModuleNot(
                response = Gson().toJson(response),
                storage = model.preferences,
                security = model.restApiSecurity,
                serializer = model.globalDataConverter,
                onError = onError,
                onSuccess = {
                    val data = model.globalDataConverter.convert(it)
                    if (data != null) {
                        onSuccess(data.message)
                    } else onError(
                        "${
                            response["message"] ?: model.resourceProvider
                                .getString(R.string.something_went_wrong)
                        }"
                    )
                }, onToken = onToken
            )
        } catch (e: Exception) {
            onError(e.message)
            e.printStackTrace()
        }
    }
}


fun productFunc(
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
            action = ActionTypeEnum.PRODUCTS.action,
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


class AccountOpeningModuleWorkSectorResponse(
    response: HashMap<String, Any?>,
    val model: RemoteViewModelImpl,
    onError: (message: String?) -> Unit,
    onSuccess: (data: MutableList<WorkSectorBean>) -> Unit,
    onToken: () -> Unit
) {
    init {
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
                            val workSector = model.sector.convert(it)
                            if (workSector != null) {
                                workSector.data?.let { it1 -> onSuccess(it1) }
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

fun workSectorFunc(
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
            action = ActionTypeEnum.SECTOR.action,
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

fun accountOpeningFunc(
    date: HashMap<String, String?>,
    officer: HashMap<String, String?>,
    accountOpening: AccountOpening,
    account: String,
    branch: String,
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
        Log.e("UNIQUE", "$unique")
        val map = APP.data(
            context = context,
            storage = model.preferences,
            action = ActionTypeEnum.RAO.action,
            uniqueId = uniqueID
        )
        map["AccountID"] = account
        map["BANKACCOUNTID"] = account
        map["INFOFIELD1"] = branch
        map["INFOFIELD4"] = accountOpening.personal?.id
        map["INFOFIELD7"] = accountOpening.personal?.firstName
        map["INFOFIELD8"] = accountOpening.personal?.secondName
        map["INFOFIELD9"] = accountOpening.personal?.thirdName
        map["INFOFIELD44"] = accountOpening.personal?.lastName
        map["INFOFIELD10"] = accountOpening.personal?.gender
        map["INFOFIELD11"] = accountOpening.personal?.dob
        map["INFOFIELD12"] = accountOpening.personal?.email
        map["INFOFIELD13"] = accountOpening.validation?.mobile
        map["INFOFIELD16"] = accountOpening.validation?.product?.get("id")
        map["INFOFIELD18"] = accountOpening.personal?.idType?.get("id")

        map["INFOFIELD15"] = accountOpening.more?.city
        map["INFOFIELD20"] = date["expires"]
        map["INFOFIELD21"] = accountOpening.more?.occupation
        map["INFOFIELD26"] = accountOpening.more?.sector //Work sector
        map["INFOFIELD27"] = accountOpening.more?.job //Job Title
        map["INFOFIELD28"] = date["issued"]
        map["INFOFIELD29"] = accountOpening.more?.employer //Employer Name
        map["INFOFIELD30"] = accountOpening.more?.monthly // Monthly Income
        map["INFOFIELD31"] = accountOpening.more?.income //Source Income
        map["INFOFIELD32"] = accountOpening.more?.residence
        map["INFOFIELD33"] = accountOpening.more?.emergency //Emergency contact number
        map["INFOFIELD34"] = accountOpening.personal?.nationality
        map["INFOFIELD35"] = accountOpening.personal?.marital
        map["INFOFIELD36"] = accountOpening.more?.education
        map["INFOFIELD37"] = accountOpening.more?.deposit //Maximum Expected Deposit Amount
        map["INFOFIELD38"] = accountOpening.more?.withdraw //Maximum Expected Withdraw Amount
        map["INFOFIELD39"] = accountOpening.more?.total //Total income
        map["INFOFIELD40"] = accountOpening.more?.country
        map["INFOFIELD41"] = accountOpening.more?.address
        map["INFOFIELD42"] = accountOpening.more?.landline
        map["INFOFIELD43"] = officer.map { it.value }.joinToString("~") //Special officer

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


fun partBase64(
    base64Image: String,
    name: String
): MultipartBody.Part {
    val imageBytes = Base64.decode(base64Image, Base64.DEFAULT)
    val requestFile = imageBytes
        .toRequestBody(
            contentType = "multipart/form-data".toMediaTypeOrNull(),
            offset = 0,
            byteCount = imageBytes.size
        )
    return MultipartBody.Part.createFormData(
        name = name,
        filename = "${name}.jpg",
        body = requestFile
    )
}