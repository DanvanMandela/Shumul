package com.craftsilicon.shumul.agency.data.permission

interface AppPermission {
    fun imageAccess(canI: (b: Boolean) -> Unit)
    fun generalAccess()

}