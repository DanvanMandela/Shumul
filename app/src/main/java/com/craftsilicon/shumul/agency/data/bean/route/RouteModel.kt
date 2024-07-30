package com.craftsilicon.shumul.agency.data.bean.route

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import javax.annotation.Nullable

data class RouteModel(
    @field:SerializedName("token")
    @field:Expose
    var token: String,
    @field:SerializedName("version")
    @field:Expose
    @Nullable
    val version: String?,
    @field:SerializedName("auth")
    @field:Expose
    @Nullable
    val auth: String?,
    @field:SerializedName("account")
    @field:Expose
    @Nullable
    val account: String?,
    @field:SerializedName("purchase")
    @field:Expose
    @Nullable
    val purchase: String?,
    @field:SerializedName("validate")
    @field:Expose
    @Nullable
    val validate: String?,
    @field:SerializedName("other")
    @field:Expose
    @Nullable
    val other: String?,
    @field:SerializedName("card")
    @field:Expose
    @Nullable
    val card: String?,
    @field:SerializedName("balance")
    @field:Expose
    @Nullable
    val balance: String?,
    @field:SerializedName("run")
    @field:Expose
    @Nullable
    var run: String?,
    @field:SerializedName("device")
    @field:Expose
    @Nullable
    var device: String?,
    @field:SerializedName("staticdata")
    @field:Expose
    @Nullable
    val staticData: String?
)
