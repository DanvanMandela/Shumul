package com.craftsilicon.shumul.agency.ui.util

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


fun Context.toast(message: String?) {
    CoroutineScope(Dispatchers.Main).launch {
        Toast.makeText(this@toast, message, Toast.LENGTH_SHORT).show()
    }
}