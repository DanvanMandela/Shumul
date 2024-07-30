package com.craftsilicon.shumul.agency.data.security

interface AppSecurity {
    fun encrypt(value: String): String
    fun encrypt(value: String, kv: String, iv: String): String

    fun decrypt(
        value: String,
        kv: String,
        iv: String,
        base64: Boolean = true
    ): String

    fun decrypt(encryptedText: String): String?
    fun base64Decrypt(str: String): String
}