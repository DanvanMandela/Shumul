package com.craftsilicon.shumul.agency.data.permission

import android.Manifest
import android.content.Context
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.craftsilicon.shumul.agency.data.permission.ImagePermissions.image
import com.craftsilicon.shumul.agency.ui.util.AppLogger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPermissionImpl @Inject constructor(private val context: Context) : AppPermission {


    override fun imageAccess(canI: (b: Boolean) -> Unit) {
        Dexter.withContext(context).withPermissions(image()).withListener(object :
            MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                if (report.areAllPermissionsGranted())
                    canI(true)
                else canI(false)
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: List<PermissionRequest>,
                token: PermissionToken
            ) {
                token.continuePermissionRequest()
            }
        }).check()
    }

    override fun generalAccess() {
        Dexter.withContext(context).withPermissions(generalPermission)
            .withListener(object :
                MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted())
                        AppLogger.instance.appLog("Permission", "General")
                    else generalAccess()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    companion object {
        private val generalPermission = arrayListOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

}