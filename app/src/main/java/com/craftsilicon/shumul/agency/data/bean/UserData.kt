package com.craftsilicon.shumul.agency.data.bean

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import javax.inject.Inject
import javax.inject.Singleton

data class UserData(
    @field:Expose
    @field:SerializedName("FirstName")
    val firstName: String?,
    @field:Expose
    @field:SerializedName("LastName")
    val lastName: String?,
    @field:Expose
    @field:SerializedName("IDNumber")
    val idNumber: String?,
    @field:Expose
    @field:SerializedName("StaticDataVersion")
    val static: String?,
    @field:Expose
    @field:SerializedName("InAPPId")
    val inAPPId: String?,
    @field:Expose
    @field:SerializedName("MobileNumber")
    val mobile: String?,
    @field:Expose
    @field:SerializedName("EMailID")
    val email: String?,
    @field:Expose
    @field:SerializedName("Accounts")
    val account: MutableList<Account>


)

data class Account(
    @field:Expose
    @field:SerializedName("AgentAccount")
    val account: String?,
    @field:Expose
    @field:SerializedName("AgentID")
    val agentID: String?,
    @field:Expose
    @field:SerializedName("IMSI")
    val msi: String?,
    @field:Expose
    @field:SerializedName("BranchID")
    val branchId: String?,
    @field:Expose
    @field:SerializedName("AccountType")
    val accountType: String?,
    @field:SerializedName("CurrencyID")
    @field:Expose
    val currency: String?,
    @field:Expose
    @field:SerializedName("DeviceID")
    val deviceID: String?,
)

@Singleton
class UserDataTypeConverter @Inject constructor(private val gson: Gson) {
    fun convert(data: UserData?): String? {
        return if (data == null) {
            null
        } else gson.toJson(data, UserData::class.java)
    }

    fun convert(data: String?): UserData? {
        return if (data == null) {
            null
        } else gson.fromJson(data, UserData::class.java)
    }

}

@Singleton
class AccountDataTypeConverter @Inject constructor(private val gson: Gson) {
    fun convert(data: Account?): String? {
        return if (data == null) {
            null
        } else gson.toJson(data, Account::class.java)
    }

    fun convert(data: String?): Account? {
        return if (data == null) {
            null
        } else gson.fromJson(data, Account::class.java)
    }

}



