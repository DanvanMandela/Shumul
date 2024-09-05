package com.craftsilicon.shumul.agency.ui.module

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.craftsilicon.shumul.agency.R
import com.google.gson.internal.LinkedTreeMap
import java.util.Locale

@Composable
fun SuccessDialog(
    message: String,
    reference: String? = null,
    action: () -> Unit
) {
    Dialog(onDismissRequest = { action() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .wrapContentSize(),
            shape = RoundedCornerShape(8.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSize))
                Text(
                    text = stringResource(id = R.string.success_),
                    fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSize))
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                )
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Text(
                    text = message,
                    fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .fillMaxWidth()
                )
                if (!reference.isNullOrBlank()) {
                    val scientificNumber = reference.toDouble()
                    val plainString = scientificNumber.toBigDecimal().toPlainString()
                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                    Text(
                        text = plainString,
                        fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                        style = MaterialTheme.typography.titleSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                )
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSize))
                Button(
                    onClick = { action() },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.close_),
                        fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

fun String.toBigNumberDisplay(): String {
    val scientificNumber = this.toDouble()
    return scientificNumber.toBigDecimal().toBigInteger().toString()
}

fun LinkedTreeMap<*, *>.toHashMap(): HashMap<String, Any?> {
    val hashMap = HashMap<String, Any?>()
    for ((key, value) in this) {
        hashMap[key.toString()] = value
    }
    return hashMap
}