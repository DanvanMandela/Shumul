package com.craftsilicon.shumul.agency.data.source.remote

import android.util.Log
import com.craftsilicon.shumul.agency.data.source.remote.util.AuthorizationInterceptor
import com.craftsilicon.shumul.agency.data.source.remote.util.StatusInterceptor
import com.craftsilicon.shumul.agency.data.source.remote.util.Timeout
import com.craftsilicon.shumul.agency.data.source.remote.util.Url.ROUTE_BASE_URL
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
class RemoteModule {

    @Provides
    fun apiService(gson: Gson, token: AuthorizationInterceptor): RemoteApiService {
        val httpLoggingInterceptor = HttpLoggingInterceptor { message ->
            Log.e(
                RemoteModule::class.simpleName,
                message
            )
        }
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(ROUTE_BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(Timeout.CONNECTION, TimeUnit.SECONDS)
                    .writeTimeout(Timeout.WRITE, TimeUnit.SECONDS)
                    .readTimeout(Timeout.READ, TimeUnit.SECONDS)
                    .addInterceptor { chain ->
                        val url = chain
                            .request()
                            .url
                            .newBuilder()
                            .build()
                        chain.proceed(chain.request().newBuilder().url(url).build())
                    }
                    .addInterceptor(token)
                    .addInterceptor(httpLoggingInterceptor)
                    .addInterceptor(StatusInterceptor())
                    .build()
            ).build()

        return retrofit.create(RemoteApiService::class.java)
    }

}