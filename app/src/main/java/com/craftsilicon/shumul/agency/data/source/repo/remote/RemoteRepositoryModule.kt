package com.craftsilicon.shumul.agency.data.source.repo.remote

import com.craftsilicon.shumul.agency.data.source.remote.RemoteApiService
import com.craftsilicon.shumul.agency.data.source.remote.RemoteSourceImpl
import com.craftsilicon.shumul.agency.data.source.scope.Remote
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RemoteRepositoryModule {
    @Provides
    @Remote
    fun provideRemote(service: RemoteApiService): RemoteSource = RemoteSourceImpl(service)
}