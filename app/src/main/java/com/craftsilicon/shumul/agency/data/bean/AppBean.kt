package com.craftsilicon.shumul.agency.data.bean

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import javax.inject.Inject
import javax.inject.Singleton

@Entity
data class AppBean(
    @PrimaryKey
    val id: String
)


data class AppUserState(
    @field:Expose
    @field:SerializedName("agent")
    val agent: String,
    @field:Expose
    @field:SerializedName("mobile")
    val mobile: String
)


@Singleton
class AppUserStateTypeConverter @Inject constructor(private val gson: Gson) {
    fun convert(data: AppUserState?): String? {
        return if (data == null) {
            null
        } else gson.toJson(data, AppUserState::class.java)
    }

    fun convert(data: String?): AppUserState? {
        return if (data == null) {
            null
        } else gson.fromJson(data, AppUserState::class.java)
    }

}
