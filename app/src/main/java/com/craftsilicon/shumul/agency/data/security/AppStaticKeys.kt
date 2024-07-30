package com.craftsilicon.shumul.agency.data.security

open class AppStaticKeys {

    external fun iv(): String
    external fun kv(): String
    external fun longKeyValue(): String
    external fun secretKey(): String
    external fun publicKey(): String

    companion object {
        init {
            System.loadLibrary("native-lib")
        }
    }
}