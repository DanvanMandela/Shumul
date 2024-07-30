package com.craftsilicon.shumul.agency.data.security

import android.util.Base64
import com.craftsilicon.shumul.agency.data.security.crypt.Crypt
import java.nio.charset.StandardCharsets
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AppSecurityImpl @Inject constructor(private val crypt: Crypt) : AppSecurity {


    private val keys = AppStaticKeys()
    private val key = crypt.sha256(keys.longKeyValue(), 32)

    override fun encrypt(value: String): String {
        return try {
            crypt.encrypt(value, key!!, keys.iv())!!
                .replace("\n", "")
        } catch (e: Exception) {
            e.printStackTrace()
            return String()
        }
    }

    override fun encrypt(value: String, kv: String, iv: String): String {
        return try {
            crypt.encrypt(value, CryptLib.SHA256(kv, 32), iv)!!
                .replace("\\r\\n|\\r|\\n", "")
        } catch (e: Exception) {
            e.printStackTrace()
            return String()
        }
    }


    override fun decrypt(encryptedText: String): String? {
        return try {
            crypt.decrypt(encryptedText, key!!, keys.iv())!!
        } catch (e: Exception) {
            e.printStackTrace()
            return String()
        }
    }

    override fun decrypt(
        value: String,
        kv: String,
        iv: String,
        base64: Boolean
    ): String {
        return try {
            crypt.decrypt(
                value,
                "${crypt.sha256(kv, 32)}",
                iv
            )!!.let {
                if (base64) base64Decrypt(it) else it
            }
        } catch (e: Exception) {
            e.printStackTrace()
            String()
        }
    }

    override fun base64Decrypt(str: String): String {
        return try {
            String(Base64.decode(str, Base64.DEFAULT), StandardCharsets.UTF_8)
        } catch (e: Exception) {
            throw e
        }
    }
}