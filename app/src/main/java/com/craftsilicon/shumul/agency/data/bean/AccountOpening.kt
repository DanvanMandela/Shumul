package com.craftsilicon.shumul.agency.data.bean

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import javax.inject.Inject
import javax.inject.Singleton

data class AccountOpening(
    @field:Expose
    var validation: CustomerValidation?,
    @field:Expose
    var personal: PersonalDetail? = null,
    @field:Expose
    var more: MoreDetailsBean? = null
)


data class CustomerValidation(
    @field:Expose
    val mobile: String?,
    @field:Expose
    val product: HashMap<String, Any?>
)


data class PersonalDetail(
    @field:Expose
    val firstName: String?,
    @field:Expose
    val secondName: String?,
    @field:Expose
    val thirdName: String?,
    @field:Expose
    val lastName: String?,
    @field:Expose
    val dob: String?,
    @field:Expose
    val id: String?,
    @field:Expose
    val idType: HashMap<String, Any?> = hashMapOf(),
    @field:Expose
    val email: String?,
    @field:Expose
    val gender: String?,
    @field:Expose
    val marital: String?,
    val nationality: String?,
)

@Singleton
class AccountOpeningTypeConverter @Inject constructor(private val gson: Gson) {
    fun convert(data: AccountOpening?): String? {
        return if (data == null) {
            null
        } else gson.toJson(data, AccountOpening::class.java)
    }

    fun convert(data: String?): AccountOpening? {
        return if (data == null) {
            null
        } else gson.fromJson(data, AccountOpening::class.java)
    }

}
