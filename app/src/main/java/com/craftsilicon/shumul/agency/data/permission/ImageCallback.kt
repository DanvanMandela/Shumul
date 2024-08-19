package com.craftsilicon.shumul.agency.data.permission

import android.graphics.Bitmap

interface ImageCallback {
    fun onImage(call: ImageCallback) {
        //TODO to be implemented
    }

    fun onImage(bitmap: Bitmap?, uri: String) {
        //TODO to be implemented
    }

    fun image(uri: String) {}
}
