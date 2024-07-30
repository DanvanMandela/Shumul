package com.craftsilicon.shumul.agency.data.source.storage.pref

import android.content.SharedPreferences
import com.craftsilicon.shumul.agency.data.bean.Account
import com.craftsilicon.shumul.agency.data.bean.AccountOpening
import com.craftsilicon.shumul.agency.data.bean.UserData
import com.craftsilicon.shumul.agency.data.bean.device.DeviceData

import kotlinx.coroutines.flow.StateFlow

interface StorageDataSource {
    val sharedPreferences: SharedPreferences

    fun userData(value: UserData)
    val userData: StateFlow<UserData?>

    fun currentAccount(value: Account)
    val currentAccount: StateFlow<Account?>

    fun login(value: Boolean)
    val login: StateFlow<Boolean>

    fun setUniqueID(value: String)
    val uniqueID: StateFlow<String?>

    fun setDeviceData(value: DeviceData)
    val deviceData: StateFlow<DeviceData?>

    fun sessionID(value: String)
    val sessionID: StateFlow<String?>

    fun helloWorld(value: String)
    val helloWorld: StateFlow<String?>


    fun accountOpen(value: AccountOpening)
    fun accountOpen()
    val accountOpen: StateFlow<AccountOpening?>

    fun setTimeout(value: Long?)
    val timeout: StateFlow<Long?>

    fun setInactivity(value: Boolean?)
    val inActivity: StateFlow<Boolean?>

}

