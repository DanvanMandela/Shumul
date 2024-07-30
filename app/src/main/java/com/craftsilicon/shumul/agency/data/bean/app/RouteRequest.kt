package com.craftsilicon.shumul.agency.data.bean.app

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RouteRequest(
    @field:SerializedName("UniqueId")
    @field:Expose
    var uniqueId: String,
    @field:SerializedName("MobileNumber")
    @field:Expose
    var mobileNumber: String,
    @field:SerializedName("Device")
    @field:Expose
    var device: String,
    @field:SerializedName("codeBase")
    @field:Expose
    var codeBase: String,
    @field:SerializedName("lat")
    @field:Expose
    var lat: String,
    @field:SerializedName("rashi")
    @field:Expose
    var rashi: String,
    @field:SerializedName("longit")
    @field:Expose
    var long: String,
    @field:SerializedName("appName")
    @field:Expose
    var appName: String,
    @field:SerializedName("IV")
    @field:Expose
    var iv: String,
    @field:SerializedName("KV")
    @field:Expose
    var kv: String
)


//Country: YEMENTEST
//
//BankID: 9981
//
//Bank Name: SHUMUL
//
//APPName: SHUMUL (Dynamic App)
//
//Data Base: Shumul
//
//Elma Core: Elma_V4
//
//Hosting: Client Environment (Yemen)
