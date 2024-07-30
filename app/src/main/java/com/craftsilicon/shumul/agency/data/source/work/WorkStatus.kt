package com.craftsilicon.shumul.agency.data.source.work

interface WorkStatus {
    fun workDone(b: Boolean) {
        throw Exception("Not implemented")
    }

    fun progress(p: Int) {
        throw Exception("Not implemented")
    }
}