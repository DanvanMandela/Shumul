package com.craftsilicon.shumul.agency.data.bean.action

enum class ActionTypeEnum(val action: String) {
    VALIDATE("ACCOUNTVALIDATION"),
    LOGIN("LOGIN"),
    BALANCE("BALANCE"),
    DEPOSIT("DEPOSITSELF"),
    WITHDRAWAL("WITHDRAWAL"),
    MINI("MINISTATEMENT"),
    FULL("FULLSTATEMENT"),
    PRODUCTS("GETPRODUCTS"),
    SECTOR("GETSECTOR"),
    CHANGE_PIN("PINCHANGE"),
    TRANSFER_OTP("TRANSFEROTP"),
    COMPLETE_TRANSFER_OTP("COMPLETETRANSFEROTP"),
    INTERNAL_TRANSFER_CUSTOMER("INTERNALTRANSFERPOSTAC"),
    RAO("RAO"),
    ACCOUNT_TO_ACCOUNT("A2A"),
    CASH_TO_CASH_GENERATE("C2C"),
    CASH_TO_CASH_REDEEM("C2CRECEIVE")

}