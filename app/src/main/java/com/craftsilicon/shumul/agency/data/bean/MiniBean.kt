package com.craftsilicon.shumul.agency.data.bean

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import javax.inject.Inject
import javax.inject.Singleton

data class MiniBean(
    @field:Expose
    @field:SerializedName("Particulars")
    val particulars: String?,
    @field:Expose
    @field:SerializedName("Debit")
    val debit: String?,
    @field:Expose
    @field:SerializedName("Credit")
    val credit: String?,

    @field:Expose
    @field:SerializedName("TrxTypeID")
    val trxTypeID: String?,
    @field:Expose
    @field:SerializedName("Amount")
    val amount: String?,
    @field:Expose
    @field:SerializedName("OperatorID")
    val operatorID: String?,
    @field:Expose
    @field:SerializedName("Bankreference")
    val bankReference: String?,
    @field:Expose
    @field:SerializedName("Transferfromname")
    val fromName: String?,

    @field:Expose
    @field:SerializedName("Transfertoname")
    val toName: String?,

    @field:Expose
    @field:SerializedName("Transferfromaccount")
    val fromAccount: String?,

    @field:Expose
    @field:SerializedName("Transfertoaccount")
    val toAccount: String?,

    @field:Expose
    @field:SerializedName("FromAccountMobileNumber")
    val fromMobile: String?,

    @field:Expose
    @field:SerializedName("TransactionType")
    val trxType: String?,
    @field:Expose
    @field:SerializedName("TrxDate")
    val date: String?,
    @field:Expose
    @field:SerializedName("Closing")
    val close: String?,
    @field:Expose
    @field:SerializedName("Trxcurrency")
    val currency: String?,
)

data class MiniData(
    @field:Expose
    @field:SerializedName("Message")
    val message: String?,
    @field:Expose
    @field:SerializedName("Data")
    val data: MutableList<MiniBean>?,
) {
    var title: String = ""
}

@Singleton
class MiniDataTypeConverter @Inject constructor(private val gson: Gson) {
    fun convert(data: MiniData?): String? {
        return if (data == null) {
            null
        } else gson.toJson(data, MiniData::class.java)
    }

    fun convert(data: String?): MiniData? {
        return if (data == null) {
            null
        } else gson.fromJson(data, MiniData::class.java)
    }

}


//"TrxDate":"2024-05-31T00:00:00+03:00",
//"TrxType":"",
//"Particulars":"Opening Balance  31 May 2024",
//"Trxcurrency":"",
//"Amount":"0.00",
//"ValueDate":"2024-05-31T00:00:00+03:00",
//"Closing":"2146182368.7700",
//"Bankreference":"",
//"Transferfromname":"",
//"Transfertoname":"",
//"Transferfromaccount":"",
//"Transfertoaccount":"",
//"FromAccountMobileNumber":""

