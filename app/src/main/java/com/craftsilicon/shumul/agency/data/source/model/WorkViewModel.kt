package com.craftsilicon.shumul.agency.data.source.model

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import com.craftsilicon.shumul.agency.data.source.repo.route.work.RouteDataWorker
import com.craftsilicon.shumul.agency.data.source.storage.pref.StorageDataSource
import com.craftsilicon.shumul.agency.data.source.work.WorkMangerDataSource
import com.craftsilicon.shumul.agency.data.source.work.WorkStatus
import com.craftsilicon.shumul.agency.data.source.work.WorkerCommons
import com.craftsilicon.shumul.agency.data.source.work.WorkerCommons.IS_WORK_DONE
import com.craftsilicon.shumul.agency.ui.util.AppLogger
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WorkViewModel @Inject constructor(
    private val worker: WorkMangerDataSource,
    private val dataSource: StorageDataSource
) : ViewModel() {

    fun routeData(owner: LifecycleOwner, status: WorkStatus) {
        val routeWorker = OneTimeWorkRequestBuilder<RouteDataWorker>()
        val continuation = worker.getWorkManger()
            .beginUniqueWork(
                WorkerCommons.TAG_DATA_WORKER,
                ExistingWorkPolicy.REPLACE,
                routeWorker.build()
            )
        continuation.enqueue()
        continuation.workInfosLiveData.observe(owner) { workInfo ->
            if (!workInfo.isNullOrEmpty()) {
                val output = workInfo[0].outputData
                val value = output.getBoolean(IS_WORK_DONE, false)
                AppLogger.instance.appLog("workInfo:value", Gson().toJson(value))
                status.workDone(value)
            }
        }
    }

}