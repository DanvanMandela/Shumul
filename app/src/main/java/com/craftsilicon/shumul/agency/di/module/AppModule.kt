package com.craftsilicon.shumul.agency.di.module

import android.app.Application
import android.content.Context
import com.craftsilicon.shumul.agency.data.permission.AppPermission
import com.craftsilicon.shumul.agency.data.permission.AppPermissionImpl
import com.craftsilicon.shumul.agency.data.provider.BaseResourceProvider
import com.craftsilicon.shumul.agency.data.provider.ResourceProvider
import com.craftsilicon.shumul.agency.data.security.AppSecurity
import com.craftsilicon.shumul.agency.data.security.AppSecurityImpl
import com.craftsilicon.shumul.agency.data.security.interaction.InteractionDataSource
import com.craftsilicon.shumul.agency.data.security.interaction.UserInteractionWatcher
import com.craftsilicon.shumul.agency.data.security.crypt.Crypt
import com.craftsilicon.shumul.agency.data.security.crypt.Cryptography
import com.craftsilicon.shumul.agency.data.security.encry.RestApiSecurity
import com.craftsilicon.shumul.agency.data.security.encry.RestApiSecurityImpl
import com.craftsilicon.shumul.agency.data.security.rsa.RSAService
import com.craftsilicon.shumul.agency.data.security.rsa.RSAServiceImpl
import com.craftsilicon.shumul.agency.data.source.storage.pref.SharedPreferencesStorage
import com.craftsilicon.shumul.agency.data.source.storage.pref.StorageDataSource
import com.craftsilicon.shumul.agency.data.source.work.WorkManagerApp
import com.craftsilicon.shumul.agency.data.source.work.WorkMangerDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    abstract fun bindContext(application: Application?): Context?

    @Binds
    abstract fun provideStorage(storage: SharedPreferencesStorage): StorageDataSource


    @Binds
    abstract fun bindWorkManger(work: WorkManagerApp): WorkMangerDataSource


    @Binds
    abstract fun provideSecurity(security: AppSecurityImpl): AppSecurity

    @Binds
    abstract fun provideCryptography(crypt: Cryptography): Crypt




    @Binds
    abstract fun provideRSA(rsa: RSAServiceImpl): RSAService


    @Binds
    abstract fun bindRestApiSecurity(ras: RestApiSecurityImpl): RestApiSecurity

    @Binds
    abstract fun bindResourceProvider(resourceProvider: ResourceProvider): BaseResourceProvider


    @Binds
    abstract fun providePermission(permissionImpl: AppPermissionImpl): AppPermission


    @Binds
    abstract fun provideInteraction(userInteractionWatcher: UserInteractionWatcher): InteractionDataSource




}