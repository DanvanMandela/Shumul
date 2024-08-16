package com.craftsilicon.shumul.agency.ui.module.deposit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.craftsilicon.shumul.agency.R
import com.craftsilicon.shumul.agency.data.bean.ValidationBean
import com.craftsilicon.shumul.agency.data.permission.CameraUtil.convert
import com.craftsilicon.shumul.agency.ui.util.formatMoney

@Composable
fun DepositConfirmDialog(data: ValidationBean, action: () -> Unit, close: () -> Unit) {

    Dialog(onDismissRequest = { close() }) {
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
                    text = stringResource(id = R.string.confirm_transaction_),
                    fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .fillMaxWidth()
                )
                if (!data.avatar.isNullOrBlank()) {
                    val image = convert(data.avatar)
                    if (image != null) {
                        Spacer(modifier = Modifier.size(ButtonDefaults.IconSize))
                        Card(
                            onClick = {

                            },
                            modifier = Modifier
                                .wrapContentSize()
                                .align(Alignment.CenterHorizontally),
                            border = BorderStroke(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.primary
                            ),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            ), shape = CircleShape
                        ) {
                            Image(
                                bitmap = image.asImageBitmap(),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .padding(2.dp)
                                    .size(80.dp)
                                    .clip(CircleShape)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSize))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.account_number_),
                        fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .weight(1f)
                    )
                    Text(
                        text = "${data.account}",
                        fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .weight(1f)
                    )
                }
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.account_name_),
                        fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .weight(1f)
                    )
                    Text(
                        text = "${data.accountName}",
                        fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .weight(1f)
                    )
                }



                if (!data.mobile.isNullOrBlank()) {
                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.mobile_),
                            fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .weight(1f)
                        )
                        Text(
                            text = "${data.mobile}",
                            fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.amount_),
                        fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .weight(1f)
                    )
                    Text(
                        text = "${data.currency} ${formatMoney(data.holderAmount ?: "")}",
                        fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .weight(1f)
                    )
                }
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSize))
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { close() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Text(
                            text = stringResource(id = R.string.cancel_),
                            fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                    Button(
                        onClick = { action() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Text(
                            text = stringResource(id = R.string.confirm_),
                            fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
        }
    }
}