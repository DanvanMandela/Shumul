package com.craftsilicon.shumul.agency.data.security.crypt

import android.util.Base64
import java.nio.charset.StandardCharsets
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Cryptography @Inject constructor() : Crypt {

    override val key: ByteArray
        get() = super.key
    override val iv: ByteArray?
        get() = super.iv

    override val cipher: Cipher
        get() = super.cipher

    override fun md5(value: String): String? {
        return try {
            val digest = MessageDigest
                .getInstance("MD5")
            digest.update(value.toByteArray())
            val messageDigest = digest.digest()
            val hexString = StringBuilder()

            for (md in messageDigest) {
                var h = Integer.toHexString(0xFF and md.toInt())
                while (h.length < 2) h = "0$h"
                hexString.append(h)
            }
            hexString.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            String()
        }

    }

    override fun generateRandomIV(length: Int): String? {
        val ranGen = SecureRandom()
        val aesKey = ByteArray(16)
        ranGen.nextBytes(aesKey)
        val result = java.lang.StringBuilder()
        for (b in aesKey) {
            result.append(String.format("%02x", b))
        }
        return if (length > result.toString().length) {
            result.toString()
        } else {
            result.substring(0, length)
        }
    }

    @Throws(
        InvalidKeyException::class,
        InvalidAlgorithmParameterException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class
    )
    override fun decrypt(encryptedText: String, key: String, iv: String): String {
        return encryptDecryptT(encryptedText, key, EncryptMode.DECRYPT, iv)
    }

    @Throws(
        InvalidKeyException::class,
        InvalidAlgorithmParameterException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class
    )
    override fun encrypt(plainText: String, key: String, iv: String): String {
        return encryptDecryptT(plainText, key, EncryptMode.ENCRYPT, iv)
    }

    @Throws(NoSuchAlgorithmException::class)
    override fun sha256(text: String, length: Int): String? {
        val md = MessageDigest.getInstance("SHA-256")
        md.update(text.toByteArray(StandardCharsets.UTF_8))
        val digest = md.digest()
        val result = StringBuilder()
        for (b in digest) {
            result.append(String.format("%02x", b))
        }
        return if (length > result.toString().length)
            result.toString()
        else result.substring(0, length)
    }

    @Throws(
        InvalidKeyException::class,
        InvalidAlgorithmParameterException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class
    )
    override fun encryptDecrypt(
        text: String,
        encryptionKey: String,
        mode: EncryptMode,
        initVector: String
    ): String {
        var len: Int = encryptionKey.toByteArray(StandardCharsets.UTF_8).size

        if (encryptionKey.toByteArray(StandardCharsets.UTF_8).size > this.key.size)
            len = this.key.size
        var ivLen: Int = initVector.toByteArray(StandardCharsets.UTF_8).size

        if (initVector.toByteArray(StandardCharsets.UTF_8).size > this.iv!!.size)
            ivLen = this.iv!!.size

        System.arraycopy(encryptionKey.toByteArray(StandardCharsets.UTF_8), 0, this.key, 0, len)
        System.arraycopy(initVector.toByteArray(StandardCharsets.UTF_8), 0, this.iv!!, 0, ivLen)

        val keySpec = SecretKeySpec(this.key, "AES")
        val ivSpec = IvParameterSpec(this.iv!!)
        return when (mode) {
            EncryptMode.ENCRYPT -> {

                cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
                val results: ByteArray =
                    cipher.doFinal(text.toByteArray(StandardCharsets.UTF_8))

                Base64.encodeToString(results, Base64.DEFAULT)
            }

            EncryptMode.DECRYPT -> {
                cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
                val decodedValue: ByteArray = Base64.decode(
                    text.toByteArray(),
                    Base64.DEFAULT
                )
                val decryptedVal: ByteArray = cipher.doFinal(decodedValue)
                String(decryptedVal)
            }
        }
    }


    override fun encryptDecryptT(
        text: String,
        encryptKey: String,
        mode: EncryptMode,
        initVector: String
    ): String {

        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        var len =
            encryptKey.toByteArray(StandardCharsets.UTF_8).size // length of the key	provided

        if (encryptKey.toByteArray(StandardCharsets.UTF_8).size > key.size)
            len = key.size

        var ivLen: Int = initVector.toByteArray(StandardCharsets.UTF_8).size

        if (initVector.toByteArray(StandardCharsets.UTF_8).size > iv!!.size)
            ivLen = iv!!.size

        System.arraycopy(encryptKey.toByteArray(StandardCharsets.UTF_8), 0, key, 0, len)
        System.arraycopy(initVector.toByteArray(StandardCharsets.UTF_8), 0, iv!!, 0, ivLen)

        val keySpec = SecretKeySpec(key, "AES")
        val ivSpec = IvParameterSpec(iv!!)

        return when (mode) {
            EncryptMode.ENCRYPT -> {
                cipher!!.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
                val results: ByteArray =
                    cipher.doFinal(text.toByteArray())

                Base64.encodeToString(results, Base64.DEFAULT)
            }

            EncryptMode.DECRYPT -> {
                cipher!!.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
                val decodedValue: ByteArray = Base64.decode(
                    text.toByteArray(),
                    Base64.DEFAULT
                )
                val decryptedVal: ByteArray = cipher.doFinal(decodedValue)
                String(decryptedVal)
            }
        }

    }

}