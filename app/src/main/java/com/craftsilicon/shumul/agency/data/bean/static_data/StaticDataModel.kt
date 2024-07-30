package com.craftsilicon.shumul.agency.data.bean.static_data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName




data class StaticDataModel(
    @field:SerializedName("Status")
    @field:Expose
    var status: String,
    @field:SerializedName("Message")
    @field:Expose
    var message: String?,

    @field:SerializedName("Version")
    @field:Expose
    var version: String?,

    @field:SerializedName("PinFormat")
    @field:Expose
    var passwordType: String?,

    @field:SerializedName("UserCode")
    @field:Expose
    var userCode: List<StaticDataDetails>?,

    @field:SerializedName("AppIdleTimeout")
    @field:Expose
    var appIdleTimeout: String?,

    @field:SerializedName("AppRateLoginCount")
    @field:Expose
    var rateMax: String?,

    @field:SerializedName("OnlineAccountProduct")
    @field:Expose
    var accountProduct: List<OnlineAccountProduct>?,

    @field:SerializedName("BankBranch")
    @field:Expose
    var bankBranch: List<OnlineAccountProduct>?,
)



data class OnlineAccountProduct(
    @field:SerializedName("ID")
    @field:Expose
    var id: String,

    @field:SerializedName("Description")
    @field:Expose
    var description: String?,

    @field:SerializedName("RelationID")
    @field:Expose
    var relationID: String?,

    @field:SerializedName("Urls")

    @field:Expose
    var urls: String?
)


