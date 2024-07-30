package com.craftsilicon.shumul.agency.ui.module

sealed class ModuleCall

sealed class Response : ModuleCall() {
    data object Confirm : Response()
    data class Success(val data: HashMap<String, Any?>) : Response()
}
