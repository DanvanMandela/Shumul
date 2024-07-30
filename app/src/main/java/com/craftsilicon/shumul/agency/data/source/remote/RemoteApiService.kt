package com.craftsilicon.shumul.agency.data.source.remote


import com.craftsilicon.shumul.agency.data.bean.app.route.PayloadRequest
import com.craftsilicon.shumul.agency.data.bean.app.route.RouteResponse
import com.craftsilicon.shumul.agency.data.source.repo.remote.RemoteSource
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Url

interface RemoteApiService : RemoteSource {

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("api/auth/app")
    override fun auth(
        @Body data: PayloadRequest
    ): Single<RouteResponse>

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST
    override fun web(
        @Url path: String,
        @Header("T") token: String,
        @Body data: PayloadRequest
    ): Single<HashMap<String, Any?>>


    @Multipart
    @POST
    override fun uploadImage(
        @Part image: MultipartBody.Part,
        @Part idFront: MultipartBody.Part,
        @Part idBack: MultipartBody.Part,
        @Part signature: MultipartBody.Part,
        @Header("UniqueID") unique: String,
        @Header("CustomerID") customerId: String,
        @Header("MobileNumber") mobileNumber: String,
        @Header("CustomerMobileNumber") customerNumber: String,
        @Header("Country") country: String,
        @Header("BankID") bankId: String,
        @Header("UploadTag") photo: String,
        @Url path: String,
    ): Single<HashMap<String, Any?>>

}