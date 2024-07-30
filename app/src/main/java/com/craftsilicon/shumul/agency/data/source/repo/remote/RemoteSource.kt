package com.craftsilicon.shumul.agency.data.source.repo.remote

import com.craftsilicon.shumul.agency.data.bean.app.route.PayloadRequest
import com.craftsilicon.shumul.agency.data.bean.app.route.RouteResponse
import io.reactivex.Single
import okhttp3.MultipartBody


interface RemoteSource {
    fun auth(data: PayloadRequest): Single<RouteResponse>
    fun web(path: String, token: String, data: PayloadRequest): Single<HashMap<String, Any?>>

    fun uploadImage(
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
    ): Single<HashMap<String, Any?>>


}

