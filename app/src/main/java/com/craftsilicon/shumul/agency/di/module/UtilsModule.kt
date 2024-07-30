package com.craftsilicon.shumul.agency.di.module

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.craftsilicon.shumul.agency.data.source.local.database.ApplicationDatabase

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.craftsilicon.shumul.agency.data.source.local.database.DatabaseHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class UtilsModule {

    @Provides
    fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.excludeFieldsWithoutExposeAnnotation()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        return gsonBuilder.create()
    }

    @Provides
    fun provideDatabase(context: Context): ApplicationDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            ApplicationDatabase::class.java,
            DatabaseHelper.NAME
        ).addCallback(object : RoomDatabase.Callback() {
        }).fallbackToDestructiveMigration().build()
    }

}