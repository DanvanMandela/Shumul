package com.craftsilicon.shumul.agency.ui.module.account

import android.graphics.Bitmap
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
import com.craftsilicon.shumul.agency.data.bean.AccountOpening

@Composable
fun AccountOpeningDialog(
    selfie: Bitmap,
    user: AccountOpening?,
    action: () -> Unit,
    close: () -> Unit
) {
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
                    text = stringResource(id = R.string.confirm_user_data_),
                    fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .fillMaxWidth()
                )
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
                        bitmap = selfie.asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(2.dp)
                            .size(80.dp)
                            .clip(CircleShape)
                    )
                }
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSize))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = stringResource(id = R.string.names_),
                        fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .weight(1f)
                    )
                    Text(
                        text = "${user?.personal?.firstName} ${user?.personal?.secondName ?: ""} " +
                                "${user?.personal?.thirdName} ${user?.personal?.lastName}",
                        fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .weight(1f)
                    )
                }
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.gender_),
                        fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .weight(1f)
                    )
                    Text(
                        text = "${user?.personal?.gender}",
                        fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .weight(1f)
                    )
                }
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.date_of_birth_),
                        fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .weight(1f)
                    )
                    Text(
                        text = "${user?.personal?.dob}",
                        fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .weight(1f)
                    )
                }
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.identification_document_),
                        fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .weight(1f)
                    )
                    Text(
                        text = "${user?.personal?.idType?.get("type")}",
                        fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .weight(1f)
                    )
                }
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.id_number_),
                        fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .weight(1f)
                    )
                    Text(
                        text = "${user?.personal?.id}",
                        fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .weight(1f)
                    )
                }
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.mobile_number_),
                        fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .weight(1f)
                    )
                    Text(
                        text = "${user?.validation?.mobile}",
                        fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .weight(1f)
                    )
                }
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.email_address_),
                        fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .weight(1f)
                    )
                    Text(
                        text = "${user?.personal?.email}",
                        fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .weight(1f)
                    )
                }
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.product_),
                        fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .weight(1f)
                    )
                    Text(
                        text = "${user?.validation?.product?.get("product")}",
                        fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .weight(1f)
                    )
                }
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSize))
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { close()},
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Text(
                            text = stringResource(id = R.string.cancel_),
                            fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall,
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
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
            }
        }
    }
}