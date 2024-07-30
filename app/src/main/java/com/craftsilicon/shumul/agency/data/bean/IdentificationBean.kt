package com.craftsilicon.shumul.agency.data.bean

import com.craftsilicon.shumul.agency.ui.util.isArabic

data class IdentificationBean(
    val id: String,
    val type: String
)


val identificationType = mutableListOf(
    IdentificationBean(
        id = "7", type = "شهادة الميلاد"
    ),
    IdentificationBean(
        id = "4", type = "رخصة قيادة"
    ),
    IdentificationBean(
        id = "5", type = "البطاقة الشخصية"
    ),
    IdentificationBean(
        id = "6", type = "جواز السفر"
    ),
    IdentificationBean(
        id = "9", type = "السجل التجاري"
    ),
    IdentificationBean(
        id = "23", type = "شهادة التأسيس"
    ),
    IdentificationBean(
        id = "25", type = "رقم البطاقة الضريبية"
    )
)


val identificationTypeEng = mutableListOf(
    IdentificationBean(
        id = "7", type = "BIRTH CERTIFICATE"
    ),
    IdentificationBean(
        id = "4", type = "DRIVING LICENSE"
    ),
    IdentificationBean(
        id = "5", type = "NATIONAL ID"
    ),
    IdentificationBean(
        id = "6", type = "PASSPORT"
    ),
    IdentificationBean(
        id = "9", type = "REGISTRATION CERTIFICATE"
    ),
    IdentificationBean(
        id = "23", type = "INCORPORATION CERTIFICATE"
    ),
    IdentificationBean(
        id = "25", type = "REVENUE AUTHORITY PIN NUMBER"
    )
)

val identificationList = if (isArabic()) identificationType else identificationTypeEng





