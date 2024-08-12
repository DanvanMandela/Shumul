package com.craftsilicon.shumul.agency.data.source.remote.util

import com.craftsilicon.shumul.agency.data.security.APP.TEST
import com.craftsilicon.shumul.agency.data.security.APP.TEST_CUSTOMER


object Timeout {
    const val CONNECTION = (30 * 1000).toLong()
    const val READ = (60 * 1000).toLong()
    const val WRITE = (60 * 1000).toLong()
}

object Url {
    private const val UAT = "https://uat.craftsilicon.com/ElmaV4Auth/"
    private const val UAT_CLIENT = "https://uatapp.shumulbankfim.com//ElmaV4AuthUat/"
    private const val LIVE_CLIENT = "https://app.shumulbankfim.com/ElmaV4Auth/"
    var ROUTE_BASE_URL = if (TEST) UAT else if (TEST_CUSTOMER) UAT_CLIENT else LIVE_CLIENT

}

