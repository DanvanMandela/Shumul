package com.craftsilicon.shumul.agency.data.security.rsa

import java.security.Key
import java.security.PrivateKey

interface RSAService {
    fun loadPublicKey(stored: String): Key
    fun encryptMessage(plainText: String, publicKey: String): String
    fun decryptMessage(encryptedText: String?, privateKey: String): String
    fun loadPrivateKey(key64: String): PrivateKey
    fun deriveKeyFromPassword(): String
    fun generateRandomIV(): String

    fun decrypt(
        encryptedString: String,
        encryptionIv: String,
        encryptionKey: String
    ): String
}



