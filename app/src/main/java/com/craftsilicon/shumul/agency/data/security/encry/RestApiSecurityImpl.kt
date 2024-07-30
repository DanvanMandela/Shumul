package com.craftsilicon.shumul.agency.data.security.encry

import android.util.Base64
import com.craftsilicon.shumul.agency.R
import com.craftsilicon.shumul.agency.data.provider.BaseResourceProvider
import com.craftsilicon.shumul.agency.data.security.CryptLib
import com.craftsilicon.shumul.agency.data.security.crypt.Crypt
import com.craftsilicon.shumul.agency.data.security.encry.RestApiSecurity
import com.craftsilicon.shumul.agency.data.source.storage.pref.StorageDataSource
import java.nio.charset.StandardCharsets
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RestApiSecurityImpl @Inject constructor(
    private val crypt: Crypt,
    private val resource: BaseResourceProvider,
    private val storage: StorageDataSource,
) : RestApiSecurity {

    override fun encrypt(value: String): String {
        TODO("Not yet implemented")
    }

    override fun encrypt(value: String, kv: String, iv: String): String {
        return try {

            val cryptLib = CryptLib()
            val key = crypt.sha256("${storage.deviceData.value?.device}", 32)
            var encrypted = cryptLib.encrypt(value, key, iv)
            encrypted = encrypted!!.replace("\\r\\n|\\r|\\n", "")
            return encrypted
        } catch (e: Exception) {
            e.printStackTrace()
            resource.getString((R.string.global_error_))
        }
    }

    override fun decrypt(
        value: String,
        kv: String,
        iv: String,
        base64: Boolean
    ): String {
        return try {
            val cryptLib = CryptLib()
            val key = crypt.sha256("${storage.deviceData.value?.device}", 32)
            cryptLib.decrypt(value, key!!, iv).let {
                if (it.isNullOrBlank())
                    throw Exception(resource.getString((R.string.global_error_)))
                else if (base64) base64Decrypt(it) else it
            }
        } catch (e: Exception) {
            e.printStackTrace()
            resource.getString((R.string.global_error_))
        }
    }

    override fun decrypt(encryptedText: String): String? {
        return ""
    }

    override fun base64Decrypt(str: String): String {
        return try {
            String(Base64.decode(str, Base64.DEFAULT), StandardCharsets.UTF_8)
        } catch (e: Exception) {
            throw e
        }
    }
}