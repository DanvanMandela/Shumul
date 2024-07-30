package com.craftsilicon.shumul.agency.ui.util

import android.util.Log
import com.craftsilicon.shumul.agency.data.security.APP.TEST
import com.craftsilicon.shumul.agency.data.security.APP.TEST_CUSTOMER
import com.craftsilicon.shumul.agency.data.security.APP.TEST_VENDOR

class AppLogger {
    companion object {
        private var INSTANCE: AppLogger? = null

        @get:Synchronized
        val instance: AppLogger
            get() {
                if (INSTANCE == null) {
                    INSTANCE = AppLogger()
                }
                return INSTANCE!!
            }

    }

    fun appLog(tag: String, message: String) {
        if (TEST || TEST_VENDOR || TEST_CUSTOMER) {
            Log.e(tag, message)
        }
    }
}