package com.craftsilicon.shumul.agency.data.source.repo.remote


import com.craftsilicon.shumul.agency.data.bean.app.route.PayloadRequest
import com.craftsilicon.shumul.agency.data.bean.app.route.RouteResponse
import com.craftsilicon.shumul.agency.data.source.scope.Remote
import io.reactivex.Single
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteRepository @Inject constructor(
    @param:Remote private val service: RemoteSource
) : RemoteSource {

    override fun auth(data: PayloadRequest): Single<RouteResponse> =
        service.auth(data)

    override fun web(
        path: String,
        token: String,
        data: PayloadRequest
    ): Single<HashMap<String, Any?>> =
        service.web(path, token, data)

    override fun uploadImage(
        image: MultipartBody.Part,
        idFront: MultipartBody.Part,
        idBack: MultipartBody.Part,
        signature: MultipartBody.Part,
        unique: String,
        customerId: String,
        mobileNumber: String,
        customerNumber: String,
        country: String,
        bankId: String,
        photo: String,
        path: String
    ): Single<HashMap<String, Any?>> = service.uploadImage(
        image,
        idFront,
        idBack,
        signature,
        unique,
        customerId,
        mobileNumber,
        customerNumber,
        country,
        bankId,
        photo,
        path
    )


}