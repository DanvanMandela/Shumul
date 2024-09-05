package com.craftsilicon.shumul.agency.data.bean

import com.craftsilicon.shumul.agency.R
import com.craftsilicon.shumul.agency.data.bean.app.response.GlobalResponseModule
import com.craftsilicon.shumul.agency.data.source.model.RemoteViewModelImpl
import com.craftsilicon.shumul.agency.ui.util.AppLogger
import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import javax.inject.Inject
import javax.inject.Singleton

data class StaticData(
    @field:Expose
    @field:SerializedName("SubCode")
    val id: String?,
    @field:Expose
    @field:SerializedName("Description")
    val description: String?
)

data class StaticDataResponse(
    @field:SerializedName("Data")
    @field:Expose
    val data: MutableList<StaticData>?,
)

@Singleton
class StaticDataResponseTypeConverter @Inject constructor(private val gson: Gson) {
    fun convert(data: StaticDataResponse?): String? {
        return if (data == null) {
            null
        } else gson.toJson(data, StaticDataResponse::class.java)
    }

    fun convert(data: String?): StaticDataResponse? {
        return if (data == null) {
            null
        } else gson.fromJson(data, StaticDataResponse::class.java)
    }

}


fun staticDataResponse(
    response: HashMap<String, Any?>,
    model: RemoteViewModelImpl,
    onError: (message: String?) -> Unit,
    onSuccess: (data: MutableList<StaticData>) -> Unit,
    onToken: () -> Unit
) {
    try {
        AppLogger.instance.appLog("STATIC:RESPONSE:ENCRYPTED", Gson().toJson(response))
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
                        val currency = model.static.convert(it)
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
