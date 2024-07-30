package com.craftsilicon.shumul.agency.data.bean.app.response

import androidx.lifecycle.LifecycleOwner
import com.craftsilicon.shumul.agency.data.source.model.WorkViewModel
import com.craftsilicon.shumul.agency.data.source.work.WorkStatus
import com.craftsilicon.shumul.agency.ui.util.AppLogger

class GlobalTokenManager(
    owner: LifecycleOwner,
    model: WorkViewModel,
    onSuccess: () -> Unit
) {

    init {
        model.routeData(owner, object : WorkStatus {
            override fun workDone(b: Boolean) {
                if (b) onSuccess()
            }
            override fun progress(p: Int) {
                AppLogger.instance.appLog("DATA:Progress", "$p")
            }
        })
    }
}