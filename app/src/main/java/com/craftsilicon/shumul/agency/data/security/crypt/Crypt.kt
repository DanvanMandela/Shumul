package com.craftsilicon.shumul.agency.data.security.crypt

import javax.crypto.Cipher

interface Crypt {

    fun generateRandomIV(length: Int): String?

    fun decrypt(encryptedText: String, key: String, iv: String): String?

    fun encrypt(plainText: String, key: String, iv: String): String?

    fun sha256(text: String, length: Int): String?


    fun md5(value: String): String?


    fun encryptDecrypt(
        text: String,
        encryptionKey: String,
        mode: EncryptMode,
        initVector: String
    ): String?

    fun encryptDecryptT(
        text: String,
        encryptKey: String,
        mode: EncryptMode,
        initVector: String
    ): String?

    val key: ByteArray
        get() = ByteArray(32)
    val iv: ByteArray?
        get() = ByteArray(16)


    val cipher: Cipher
        get() = Cipher.getInstance("AES/CBC/PKCS5Padding")



}