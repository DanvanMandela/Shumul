package com.craftsilicon.shumul.agency.ui.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.craftsilicon.shumul.agency.data.security.APP.TEST_CUSTOMER
import com.craftsilicon.shumul.agency.data.security.APP.TEST_VENDOR
import java.text.DecimalFormat
import java.util.Locale

fun formatMoney(value: String): String {
    val amount: Double = value.toDouble()
    val formatter = DecimalFormat("#,###")
    return formatter.format(amount)
}

fun layoutDirection(): LayoutDirection {
    return if (isArabic())
        LayoutDirection.Rtl
    else LayoutDirection.Ltr
}

fun isArabic(): Boolean {
    val defaultLocale = Locale.getDefault()
    return defaultLocale.language == "ar"
}

fun countryCode(): String {
    return if (isArabic() || !TEST_VENDOR)
        "967"
    else "254"
}

val horizontalPadding = 48.dp

val horizontalModulePadding = 32.dp
val buttonHeight = 42.dp


class MoneyVisualTransformation : VisualTransformation {

    private val formatter = DecimalFormat("#,###")

    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        val formattedText = formatter.format(originalText.toDoubleOrNull() ?: 0.0)

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return formattedText.length
            }

            override fun transformedToOriginal(offset: Int): Int {
                return originalText.length
            }
        }

        return TransformedText(AnnotatedString(formattedText), offsetMapping)
    }
}

fun String.isValidEmail(): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
    return emailRegex.matches(this)
}

