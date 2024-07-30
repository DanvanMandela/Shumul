package com.craftsilicon.shumul.agency.data.security.rsa

import android.util.Base64
import android.util.Log
import com.craftsilicon.shumul.agency.data.security.generateAlphaNumericString
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.security.GeneralSecurityException
import java.security.Key
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Arrays
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RSAServiceImpl @Inject constructor() : RSAService {


    @Throws(GeneralSecurityException::class, IOException::class)
    override fun loadPublicKey(stored: String): Key {
        val data: ByteArray = Base64.decode(stored, Base64.NO_WRAP)
        val spec = X509EncodedKeySpec(data)
        val fact = KeyFactory.getInstance("RSA")
        return fact.generatePublic(spec)
    }

    @Throws(Exception::class)
    override fun encryptMessage(plainText: String, publicKey: String): String {
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, loadPublicKey(publicKey))
        return Base64.encodeToString(cipher.doFinal(plainText.toByteArray()), Base64.DEFAULT)
    }

    @Throws(Exception::class)
    override fun decryptMessage(encryptedText: String?, privateKey: String): String {
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.DECRYPT_MODE, loadPrivateKey(privateKey))
        return String(cipher.doFinal(Base64.decode(encryptedText, Base64.NO_WRAP)))
    }

    @Throws(GeneralSecurityException::class)
    override fun loadPrivateKey(key64: String): PrivateKey {
        val clear: ByteArray = Base64.decode(key64.toByteArray(), Base64.NO_WRAP)
        val keySpec = PKCS8EncodedKeySpec(clear)
        val fact = KeyFactory.getInstance("RSA")
        val pKey = fact.generatePrivate(keySpec)
        Arrays.fill(clear, 0.toByte())
        return pKey
    }

    override fun deriveKeyFromPassword(): String {
        return generateAlphaNumericString(16)
    }

    override fun generateRandomIV(): String {
        return generateAlphaNumericString(16)
    }


    override fun decrypt(
        encryptedString: String,
        encryptionIv: String,
        encryptionKey: String,
    ): String {
        val key = ByteArray(32) // 256 bit key space
        val iv = ByteArray(16)

        var len: Int =
            encryptionKey.toByteArray(StandardCharsets.UTF_8).size // length of the key	provided

        if (encryptionKey.toByteArray(StandardCharsets.UTF_8).size > key.size) len = key.size

        var ivlen: Int = encryptionIv.toByteArray(StandardCharsets.UTF_8).size
        if (encryptionIv.toByteArray(StandardCharsets.UTF_8).size > iv.size) ivlen = iv.size

        System.arraycopy(encryptionKey.toByteArray(StandardCharsets.UTF_8), 0, key, 0, len)
        System.arraycopy(encryptionIv.toByteArray(StandardCharsets.UTF_8), 0, iv, 0, ivlen)


        Log.e("NEW KEY", key.contentToString())
        Log.e("NEW IV", iv.contentToString())

        val cx = Cipher.getInstance("AES/GCM/NoPadding")
        cx.init(Cipher.DECRYPT_MODE, key(encryptionKey), iv(iv))
        Log.e("ENCRYPTED DATA", encryptedString)
        val decodedValue: ByteArray = Base64.decode(encryptedString.toByteArray(), Base64.DEFAULT)
        val decryptedVal: ByteArray = cx.doFinal(decodedValue)
        return String(decryptedVal)
    }

    private fun key(key: String): SecretKeySpec {
        val bytes = key.toByteArray(StandardCharsets.UTF_8)
        return SecretKeySpec(bytes, 0, bytes.size, "AES")
    }

    private fun iv(iv: ByteArray): GCMParameterSpec {
        return GCMParameterSpec(128, iv)
    }


}