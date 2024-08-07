package com.craftsilicon.shumul.agency.data.security

import android.Manifest
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import com.craftsilicon.shumul.agency.data.security.APP.TEST_VENDOR
import com.craftsilicon.shumul.agency.data.security.Util.newEncrypt
import com.craftsilicon.shumul.agency.data.source.storage.pref.StorageDataSource
import java.util.UUID


object ActivationData {
    val AGENT_ID = if (TEST_VENDOR) "10051" else "10065"
    val AGENT_PIN = if (TEST_VENDOR) "2798" else "5399"
    val MOBILE = if (TEST_VENDOR) "726924300" else "776622171"
}


fun generateAlphaNumericString(n: Int): String {
    val s = ("ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz")
    val sb = StringBuilder(n)
    for (i in 0 until n) {
        val index = (s.length
                * Math.random()).toInt()
        sb.append(s[index])
    }
    return sb.toString()
}

fun getUniqueID(): String {
    return UUID.randomUUID().toString()
}

@Suppress("DEPRECATION", "HardwareIds", "MissingPermission")
fun imeiDeviceId(context: Context): String? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    } else {
        val mTelephony =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            getUniqueID()
        }
        mTelephony.deviceId
    }
}

object APP {
    const val TEST = true
    const val TEST_VENDOR = true
    const val TEST_CUSTOMER = false
    const val ACTIVATED = true
    const val BANK_ID = "9981"
    const val CODE_BASE = "ANDROID"
    const val APP_NAME = "SHUMULAGENCY"
    val customerID = if (TEST) "2549981" else "2549981"
    val country = if (TEST || TEST_CUSTOMER) "YEMENTEST" else "YEMEN"

    fun data(
        context: Context,
        storage: StorageDataSource,
        action: String,
        uniqueId: String
    ): HashMap<String, Any?> {
        return hashMapOf(
            "FormID" to action,
            "APPNAME" to APP_NAME,
            "BANKID" to BANK_ID,
            "CustomerID" to customerID,
            "SessionID" to storage.sessionID.value,
            "VersionNumber" to appVersion(context),
            "TRXSOURCE" to "AGENCY",
            "UNIQUEID" to uniqueId,
            "CODEBASE" to CODE_BASE,
            "Country" to country,
            "IMEI" to newEncrypt(imeiDeviceId(context)!!),
            "DEVICEID" to newEncrypt(imeiDeviceId(context)!!),
            "LATLON" to "0.0,0.0"
        )
    }

    private fun appVersion(context: Context): String {
        return try {
            val packageInfo: PackageInfo = context
                .packageManager.getPackageInfo(context.packageName, 0)
            val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.longVersionCode.toString()
            } else {
                @Suppress("DEPRECATION")
                packageInfo.versionCode.toString()
            }
            versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            "Version information not available"
        }
    }
}