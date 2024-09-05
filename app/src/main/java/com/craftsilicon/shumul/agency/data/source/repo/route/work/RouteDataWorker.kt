package com.craftsilicon.shumul.agency.data.source.repo.route.work


import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Data
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import com.craftsilicon.shumul.agency.data.bean.app.ResponseSerializer
import com.craftsilicon.shumul.agency.data.bean.app.RouteRequest
import com.craftsilicon.shumul.agency.data.bean.app.StatusEnum
import com.craftsilicon.shumul.agency.data.bean.app.route.PayloadRequest
import com.craftsilicon.shumul.agency.data.bean.device.DeviceDataTypeConverter
import com.craftsilicon.shumul.agency.data.security.APP.APP_NAME
import com.craftsilicon.shumul.agency.data.security.APP.CODE_BASE
import com.craftsilicon.shumul.agency.data.security.APP.customerID
import com.craftsilicon.shumul.agency.data.security.AppStaticKeys
import com.craftsilicon.shumul.agency.data.security.getUniqueID
import com.craftsilicon.shumul.agency.data.security.imeiDeviceId
import com.craftsilicon.shumul.agency.data.security.rsa.RSAService
import com.craftsilicon.shumul.agency.data.source.repo.remote.RemoteRepository
import com.craftsilicon.shumul.agency.data.source.storage.pref.StorageDataSource
import com.craftsilicon.shumul.agency.data.source.work.WorkerCommons
import com.craftsilicon.shumul.agency.ui.util.AppLogger
import com.google.gson.Gson
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

@HiltWorker
class RouteDataWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val route: RemoteRepository,
    private val storage: StorageDataSource,
    private val rsaService: RSAService,
    private val deviceSerializer: DeviceDataTypeConverter,
    private val responseSerializer: ResponseSerializer
) : RxWorker(context, workerParameters) {
    override fun createWork(): Single<Result> {
        return try {
            val uniqueID = getUniqueID()
            storage.setUniqueID(uniqueID)
            val kv = rsaService.deriveKeyFromPassword()
            val iv = rsaService.generateRandomIV()
            val publicKey = AppStaticKeys().publicKey()
            val requestData = RouteRequest(
                uniqueId = uniqueID,
                mobileNumber = customerID,
                device = imeiDeviceId(this.applicationContext)!!,
                codeBase = CODE_BASE,
                "0.00",
                "",
                "0.00",
                appName = APP_NAME,
                kv = kv,
                iv = iv
            )

            val request = rsaService.encryptMessage(
                Gson().toJson(requestData), publicKey
            )

            AppLogger.instance.appLog("Routes:Request:CLEAN:", Gson().toJson(requestData))
            AppLogger.instance.appLog("Routes:Request:EN:", Gson().toJson(request))

            route.auth(
                data = PayloadRequest(data = request)
            ).doOnError {
                constructResponse(Result.failure())
            }.map {

                if (!it.data.isNullOrBlank()) {
                    val data = responseSerializer.convert(
                        rsaService.decrypt(
                            encryptedString = "${it.data}",
                            encryptionKey = kv,
                            encryptionIv = iv
                        )
                    )
                    Log.e("DECRYPTED:RESPONSE", Gson().toJson(data))
                    if (data != null && data.respCode == StatusEnum.SUCCESS.type) {
                        data.payload?.device?.let { s -> storage.sessionID(s) }
                        AppLogger.instance.appLog(
                            "Session",
                            "${data.payload?.device}"
                        )
                        val routes = deviceSerializer.convert(
                            rsaService.decrypt(
                                encryptedString = "${data.payload?.routes}",
                                encryptionKey = kv,
                                encryptionIv = iv
                            )
                        )
                        runBlocking {
                            storage.token("")
                            delay(200)
                            val token = data.token
                            storage.token(data.token!!)
                            routes?.token = token!!
                            routes?.run = iv
                            routes?.device = kv
                            storage.setDeviceData(routes!!)
                            AppLogger.instance.appLog("Routes:Response:", Gson().toJson(routes))
                            constructResponse(
                                Result.success(
                                    Data.Builder().putBoolean(WorkerCommons.IS_WORK_DONE, true)
                                        .build()
                                )
                            )
                        }
                    } else constructResponse(Result.retry())

                } else constructResponse(Result.retry())
            }
                .onErrorReturn {
                    constructResponse(Result.retry())
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
        } catch (e: Exception) {
            e.localizedMessage?.let { AppLogger.instance.appLog("Routes", it) }
            e.printStackTrace()
            Single.just(Result.failure())
        }
    }

    private fun constructResponse(result: Result): Result {
        return result
    }
}