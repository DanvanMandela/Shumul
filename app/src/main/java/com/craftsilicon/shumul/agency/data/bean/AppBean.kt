package com.craftsilicon.shumul.agency.data.bean

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AppBean(
    @PrimaryKey
    val id:String
)
