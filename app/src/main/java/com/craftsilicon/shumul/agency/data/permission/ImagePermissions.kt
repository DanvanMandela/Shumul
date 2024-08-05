package com.craftsilicon.shumul.agency.data.permission

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import androidx.activity.result.ActivityResultLauncher
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.zip.GZIPOutputStream

object ImagePermissions {
    fun image(): List<String> {
        val permission: MutableList<String> = ArrayList()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)
            permission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        permission.add(Manifest.permission.CAMERA)
        return permission
    }
}


object CameraUtil {

     fun Context.capturedImage(selectedPhotoUri: Uri): Bitmap {
        return when {
            Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(
                this.contentResolver,
                selectedPhotoUri
            )

            else -> {
                val source = ImageDecoder.createSource(this.contentResolver, selectedPhotoUri)
                ImageDecoder.decodeBitmap(source)
            }
        }
    }

    fun getImageFromStorage(path: String): Bitmap? {
        val f = File(path)
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = false
        //options.inSampleSize = calculateInSampleSize(options, 512, 512)
        return BitmapFactory.decodeStream(FileInputStream(f), null, options)
    }

    @JvmStatic
    fun compressImage(image: Bitmap): Bitmap? {
        return try {
            val stream = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            var options = 20
            while (stream.toByteArray().size / 1024 > 100) { // 100kb,
                stream.reset()
                image.compress(Bitmap.CompressFormat.JPEG, options, stream)
                options -= 10 // 10
            }
            val isBm = ByteArrayInputStream(
                stream.toByteArray()
            )
            BitmapFactory.decodeStream(isBm, null, null)
        } catch (e: Exception) {
            null
        }
    }

    fun compressBitmap(
        bitmap: Bitmap,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
        quality: Int = 50
    ): Bitmap {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(format, quality, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    @Throws(IllegalArgumentException::class)
    fun convert(base64Str: String): Bitmap? {
        val decodedBytes: ByteArray = Base64.decode(
            base64Str.substring(base64Str.indexOf(",") + 1),
            Base64.DEFAULT
        )
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    fun convert(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return Base64.encodeToString(
            outputStream.toByteArray(),
            Base64.DEFAULT
        )
    }

    fun convertByte(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    fun String.isBase64String(): Boolean {
        return try {
            Base64.decode(this, Base64.DEFAULT)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }

}

fun compress(string: String): ByteArray? {
    return try {
        val os = ByteArrayOutputStream(string.length)
        val gos = GZIPOutputStream(os)
        gos.write(string.toByteArray())
        gos.close()
        val compressed: ByteArray = os.toByteArray()
        os.close()
        compressed
    } catch (ex: IOException) {
        null
    }
}

fun String.compressString(): String? {
    val compressed: ByteArray = compress(this)!!
    return Base64.encodeToString(compressed, Base64.NO_WRAP)
}


fun ActivityResultLauncher<CropImageContractOptions>.imageOption() {
    this.launch(
        options {
            setGuidelines(CropImageView.Guidelines.ON)
            setImageSource(includeGallery = false, includeCamera = true)
            setOutputCompressQuality(50)
        }
    )
}


