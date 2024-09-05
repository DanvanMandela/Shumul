package com.craftsilicon.shumul.agency.data.source.model

import retrofit2.HttpException
import androidx.lifecycle.ViewModel
import com.craftsilicon.shumul.agency.data.bean.BalanceBeanTypeConverter
import com.craftsilicon.shumul.agency.data.bean.CurrencyBeanTypeConverter
import com.craftsilicon.shumul.agency.data.bean.MiniDataTypeConverter
import com.craftsilicon.shumul.agency.data.bean.ProductBeanTypeConverter
import com.craftsilicon.shumul.agency.data.bean.StaticDataResponseTypeConverter
import com.craftsilicon.shumul.agency.data.bean.UserDataTypeConverter
import com.craftsilicon.shumul.agency.data.bean.ValidationResponseTypeConverter
import com.craftsilicon.shumul.agency.data.bean.WorkSectorBeanTypeConverter
import com.craftsilicon.shumul.agency.data.bean.app.response.AnyConverter
import com.craftsilicon.shumul.agency.data.bean.app.response.GlobalResponseSerializer
import com.craftsilicon.shumul.agency.data.bean.app.response.GlobalResponseTypeConverter
import com.craftsilicon.shumul.agency.data.bean.app.response.bodyToMap
import com.craftsilicon.shumul.agency.data.bean.app.route.PayloadRequest
import com.craftsilicon.shumul.agency.data.bean.device.DeviceData
import com.craftsilicon.shumul.agency.data.permission.AppPermission
import com.craftsilicon.shumul.agency.data.provider.BaseResourceProvider
import com.craftsilicon.shumul.agency.data.security.AppSecurity
import com.craftsilicon.shumul.agency.data.security.encry.RestApiSecurity
import com.craftsilicon.shumul.agency.data.source.repo.remote.RemoteRepository
import com.craftsilicon.shumul.agency.data.source.storage.pref.StorageDataSource
import com.craftsilicon.shumul.agency.ui.navigation.ModuleState
import com.craftsilicon.shumul.agency.ui.util.AppLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class RemoteViewModelImpl @Inject constructor(
    private val repo: RemoteRepository,
    val user: UserDataTypeConverter,
    val any: AnyConverter,
    val preferences: StorageDataSource,
    val resourceProvider: BaseResourceProvider,
    val globalResponseConverter: GlobalResponseTypeConverter,
    val globalDataConverter: GlobalResponseSerializer,
    val security: AppSecurity,
    val restApiSecurity: RestApiSecurity,
    val balance: BalanceBeanTypeConverter,
    val validation: ValidationResponseTypeConverter,
    val mini: MiniDataTypeConverter,
    val permission: AppPermission,
    val product: ProductBeanTypeConverter,
    val currency: CurrencyBeanTypeConverter,
    val sector: WorkSectorBeanTypeConverter,
    val static: StaticDataResponseTypeConverter
) : ViewModel(), RemoteViewModel {

    override val deviceData: DeviceData?
        get() = preferences.deviceData.value
    override val token: String? = preferences.token.value

    override fun web(
        path: String,
        data: PayloadRequest,
        state: (state: ModuleState) -> Unit,
        onResponse: (response: HashMap<String, Any?>) -> Unit
    ) {
        val dispose = CompositeDisposable()
        val token = "${preferences.token.value}"
        dispose.add(repo.web(
            path = path,
            token = token,
            data = data
        ).doOnSubscribe { state(ModuleState.LOADING) }
            .doOnError { state(ModuleState.ERROR) }
            .doOnSuccess { }
            .subscribeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .subscribe({ onResponse(it) }, { error ->
                if (error is HttpException) {
                    val errorBody = error.response()?.errorBody()?.string()
                    onResponse(errorBody.bodyToMap())
                }
                state(ModuleState.ERROR)
            })
        )
    }

    override fun upload(
        part: HashMap<String, MultipartBody.Part>,
        data: HashMap<String, String>,
        state: (state: ModuleState) -> Unit,
        onResponse: (response: HashMap<String, Any?>) -> Unit
    ) {
        val dispose = CompositeDisposable()
        dispose.add(repo.uploadImage(
            path = data["path"]!!,
            idFront = part["idFront"]!!,
            idBack = part["idBack"]!!,
            signature = part["signature"]!!,
            image = part["passport"]!!,
            unique = data["unique"]!!,
            customerId = data["customerId"]!!,
            mobileNumber = data["mobileNumber"]!!,
            customerNumber = data["customerNumber"]!!,
            country = data["country"]!!,
            bankId = data["bankId"]!!,
            photo = data["uploadTag"]!!
        ).doOnSubscribe { state(ModuleState.LOADING) }
            .doOnError { state(ModuleState.ERROR) }
            .doOnSuccess { }
            .subscribeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .subscribe({ onResponse(it) }, { error ->
                if (error is HttpException) {
                    val errorBody = error.response()?.errorBody()?.string()
                    onResponse(errorBody.bodyToMap())
                }
                state(ModuleState.ERROR)
            })
        )
    }

}

interface RemoteViewModel {
    fun web(
        path: String,
        data: PayloadRequest,
        state: (state: ModuleState) -> Unit,
        onResponse: (response: HashMap<String, Any?>) -> Unit
    )

    fun upload(
        part: HashMap<String, MultipartBody.Part>,
        data: HashMap<String, String>,
        state: (state: ModuleState) -> Unit,
        onResponse: (response: HashMap<String, Any?>) -> Unit
    )

    val deviceData: DeviceData?

    val token: String?
}