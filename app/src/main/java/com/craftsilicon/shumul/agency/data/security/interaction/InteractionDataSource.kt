package com.craftsilicon.shumul.agency.data.security.interaction

interface InteractionDataSource {
    fun onUserInteracted() {
        throw Exception("Not implemented")
    }

    fun setTimer() {
        throw Exception("Not implemented")
    }

    fun timer(str: String)
    fun done(boolean: Boolean)
}