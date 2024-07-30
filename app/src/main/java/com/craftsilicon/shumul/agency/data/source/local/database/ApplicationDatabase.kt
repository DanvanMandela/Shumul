package com.craftsilicon.shumul.agency.data.source.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.craftsilicon.shumul.agency.data.bean.AppBean
import com.craftsilicon.shumul.agency.data.source.local.converter.LocalDateTimeConverter

@Database(
    entities = [AppBean::class],
    version = DatabaseHelper.VERSION,
    exportSchema = DatabaseHelper.EXPORTED
)
@TypeConverters(LocalDateTimeConverter::class)
abstract class ApplicationDatabase : RoomDatabase() {

}