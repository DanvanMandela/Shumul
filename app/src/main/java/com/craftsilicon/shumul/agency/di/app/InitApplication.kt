package com.craftsilicon.shumul.agency.di.app


import android.annotation.SuppressLint
import androidx.hilt.work.HiltWorkerFactory
import androidx.multidex.MultiDexApplication
import androidx.work.Configuration
import androidx.work.impl.Scheduler
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor


@HiltAndroidApp
class InitApplication : MultiDexApplication(), Configuration.Provider {
    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface HiltWorkerFactoryEntryPoint {
        fun workerFactory(): HiltWorkerFactory
    }

    @SuppressLint("RestrictedApi")
    override val workManagerConfiguration: Configuration =
        Configuration.Builder()
            .setExecutor(Dispatchers.Default.asExecutor())
            .setWorkerFactory(
                EntryPoints.get(
                    this,
                    HiltWorkerFactoryEntryPoint::class.java
                ).workerFactory()
            )
            .setTaskExecutor(Dispatchers.Default.asExecutor())
            .setMaxSchedulerLimit(Scheduler.MAX_SCHEDULER_LIMIT)
            .build()
}