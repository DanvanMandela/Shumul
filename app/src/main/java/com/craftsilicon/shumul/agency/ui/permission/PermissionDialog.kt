package com.craftsilicon.shumul.agency.ui.permission

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.craftsilicon.shumul.agency.R


@Composable
fun PermissionDialog(
    permissionTextProvider: PermissionTextProvider,
    isPermanentlyDeclined: Boolean,
    onDismiss: () -> Unit,
    onOkClick: () -> Unit,
    onGoToAppSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context= LocalContext.current
    AlertDialog(onDismissRequest = onDismiss,
        confirmButton = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.padding(10.dp))
                Text(
                    text = if (isPermanentlyDeclined) {
                        "Grant permission"
                    } else {
                        "OK"
                    },
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (isPermanentlyDeclined) {
                                onGoToAppSettingsClick()
                            } else {
                                onOkClick()
                            }
                        }
                        .padding(16.dp)
                )
            }
        }, title = { Text(text = "") }, text = {
            Text(
                text = permissionTextProvider.getDescription(
                    isPermanentlyDeclined = isPermanentlyDeclined, context = context
                )
            )
        })
}

interface PermissionTextProvider {
    fun getDescription(isPermanentlyDeclined: Boolean,context: Context): String
}

abstract class CameraPermissionTextProvider() : PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean, context: Context): String {
        return if (isPermanentlyDeclined) {
            "It seems you permanently declined camera permission. " +
                    "You can go to the app settings to grant it."
        } else context.getString(R.string.camera_permission_message_)
    }
}

